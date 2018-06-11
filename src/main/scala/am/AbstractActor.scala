package am

import java.util.concurrent.Semaphore

import am.message._
import com.typesafe.scalalogging.Logger

import scala.collection.mutable

/**
 * This is the base class for actors.
 * It provides basic implementation for
 * queuing messages and executing them concurrently.
 *
 * <p>
 * It also supports messages:
 * <ul>
 * <li>[[KillMessage]]</li>
 * <li>[[TrackMessage]]</li>
 * <li>[[TrackIdleMessage]]</li>
 * </ul>
 */
abstract class AbstractActor extends Actor {
  private def logger = AbstractActor.logger

  private val queue = new mutable.Queue[(ActorRef, Message)]()
  private val ready = new Semaphore(0)

  private val thread = new Thread(() => run())
  thread.start()

  private def defaultReference: ActorRef =
    (sender, message) => this.dispatch(sender, message)

  /**
   * This is the provider of the default message sender.
   *
   * <p>
   * Its primary purpose is to provide the implicit message sender to
   * [[ActorRef]] instances.
   */
  protected implicit def defaultSender: ActorRef = _reference

  private var _reference = defaultReference

  def reference: ActorRef = _reference

  def reference_=(ref: ActorRef): Unit = {
    _reference = ref
  }

  def stop(): Unit = thread.interrupt()

  def receive(sender: ActorRef, message: Message)

  final def dispatch(sender: ActorRef, message: Message): Unit = {
    queue.enqueue((sender, message))

    // notify this actor that a new message is in the queue
    ready.release()
  }

  private def run(): Unit = {
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

    case TrackMessage(m, c) =>
      val h = accept(sender, m)

      // send confirmation
      sender :! c

      return h

    case TrackIdleMessage(m, c) =>
      val h = accept(sender, m)

      return () => {
        h()

        // send confirmation when idle
        sender :! c
      }

    case m: Message =>
      receive(sender, m); null
    case _ => logger.error("Invalid message"); null
  }
}

object AbstractActor {
  private val logger = Logger("Actor")
}
