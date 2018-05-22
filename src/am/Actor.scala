package am

import java.net.InetSocketAddress
import java.net.SocketAddress
import am.network.RUDPServer
import am.network.ActorAddress
import am.network.MessagePacket
import scala.collection.mutable.Queue
import java.util.concurrent.Semaphore

abstract class Actor {
  private val queue = new Queue[(ActorRef, Message)]()
  private val ready = new Semaphore(0)

  private val thread = new Thread(() => run())
  thread.start()

  private var _reference = defaultReference
  def reference = _reference
  private def reference_=(ref: ActorRef) = _reference = ref

  def receive(sender: ActorRef, message: Message)

  final def dispatch(sender: ActorRef, message: Message) = {
    queue.enqueue((sender, message))

    // notify this actor that a new message is in
    //   the queue
    ready.release()
  }

  private def run() = {
    while (!Thread.interrupted()) {
      // wait for a message
      ready.acquire()

      val (sender, message) = queue.dequeue()

      message match {
        case m: Message => receive(sender, m)
        case _ => println("Invalid message")
      }
    }
  }

  private def defaultReference: ActorRef =
    (sender, message) => this.dispatch(sender, message)
}

object Actor {
  def ignore(): ActorRef =
    (sender, message) => ()
}
