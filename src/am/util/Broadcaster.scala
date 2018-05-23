package am.util

import am.Actor
import am.ActorRef
import am.message.Message
import java.util.ArrayList

class Broadcaster extends Actor {
  private val targets = new ArrayList[ActorRef]()

  def add(actor: ActorRef) = targets.add(actor)
  def remove(actor: ActorRef) = targets.remove(actor)
  
  def receive(sender: ActorRef, message: Message): Unit =
    targets.forEach(a => a.send(sender, message))
}
