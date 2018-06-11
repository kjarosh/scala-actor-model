package am.util

import java.util

import am.{AbstractActor, ActorRef}
import am.message.Message

class Broadcaster extends AbstractActor {
  private val targets = new util.ArrayList[ActorRef]()

  def add(actor: ActorRef): Boolean = targets.add(actor)

  def remove(actor: ActorRef): Boolean = targets.remove(actor)

  def receive(sender: ActorRef, message: Message): Unit =
    targets.forEach(a => a.send(sender, message))
}
