package am

import java.net.InetSocketAddress
import java.net.SocketAddress
import am.network.RUDPServer
import am.network.ActorAddress
import am.network.MessagePacket
import scala.collection.mutable.Queue
import java.util.concurrent.Semaphore
import am.message._

abstract class Actor {
  private def logger = Actor.logger
  
  private val queue = new Queue[(ActorRef, Message)]()
  private val ready = new Semaphore(0)

  private val thread = new Thread(() => run())
  thread.start()

  private def defaultReference: ActorRef =
    (sender, message) => this.dispatch(sender, message)

  private var _defaultSender = Actor.ignore
  protected implicit def defaultSender = _reference //_defaultSender

  private var _reference = defaultReference
  def reference = _reference
  def reference_=(ref: ActorRef) = {
    _reference = ref
    _defaultSender = ref
  }

  def stop() = thread.interrupt()

  def receive(sender: ActorRef, message: Message)

  final def dispatch(sender: ActorRef, message: Message) = {
    queue.enqueue((sender, message))

    // notify this actor that a new message is in the queue
    ready.release()
  }

  private def run() = {
    var idleHandler: () => Unit = null

    while (!Thread.interrupted()) {
      // wait for a message
      if (!ready.tryAcquire()) {
        if (idleHandler != null) idleHandler()
        ready.acquire()
      }

      val (sender, message) = queue.dequeue()

      idleHandler = accept(sender, message)
    }
  }

  private def accept(sender: ActorRef, obj: Object): () => Unit = obj match {
    case KillMessage() =>
      this.stop(); null

    case TrackMessage(m, c) => {
      val h = accept(sender, m)

      // send confirmation
      sender :! c

      return h
    }

    case TrackIdleMessage(m, c) => {
      val h = accept(sender, m)

      return () => {
        h()

        // send confirmation when idle
        sender :! c
      }
    }

    case m: Message =>
      receive(sender, m); null
    case _ => logger.err("Invalid message"); null
  }
}

object Actor {
  private val logger = new Logger("Actor")
  
  def ignore(): ActorRef = (sender, message) => ()
}
