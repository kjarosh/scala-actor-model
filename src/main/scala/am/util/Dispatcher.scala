package am.util

import am.message.{Message, TrackIdleMessage}
import am.{AbstractActor, ActorRef}

import scala.collection.mutable

private case class TargetFreeMessage(id: Int) extends Message

class Dispatcher extends AbstractActor {
  private var nextId = 1
  private val targetsFree = new mutable.HashMap[Int, Boolean]()
  private val targetsById = new mutable.HashMap[Int, ActorRef]()
  private val targets = new mutable.HashMap[ActorRef, Int]()

  private val lock = new Object()

  def add(actor: ActorRef): Option[Boolean] = lock.synchronized {
    if (nextId == 0) nextId += 1

    val id = nextId
    nextId += 1
    targets.put(actor, id)
    targetsById.put(id, actor)
    targetsFree.put(id, true)
  }

  def remove(actor: ActorRef): Boolean = lock.synchronized {
    val id = targets(actor)

    targets.remove(actor)
    targetsById.remove(id)
    targetsFree.remove(id)

    true
  }

  private def firstFree(): Int = lock.synchronized {
    for ((id, free) <- targetsFree.iterator) if (free) {
      return id
    }

    return 0
  }

  private val toHandle = new mutable.Queue[(ActorRef, Message)]()

  private def handleEnqueued(): Unit = lock.synchronized {
    var id = firstFree()
    while (id != 0) {
      val (sender, message) = toHandle.dequeue()
      handle(id, sender, message)

      id = firstFree()
    }
  }

  private def handle(id: Int, sender: ActorRef, message: Message): Unit = {
    val finalMessage = TrackIdleMessage(message, TargetFreeMessage(id))
    targetsById(id).send(sender, finalMessage)
    targetsFree(id) = false
  }

  def receive(sender: ActorRef, message: Message): Unit = lock.synchronized {
    message match {
      case TargetFreeMessage(id) =>
        targetsFree(id) = true

        handleEnqueued()

      case _ =>
        val id = firstFree()
        if (id != 0) {
          handle(id, sender, message)
          return
        }

        // no free actor
        toHandle.enqueue((sender, message))
    }
  }
}
