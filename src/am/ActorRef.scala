package am

import am.message.Message

@FunctionalInterface
trait ActorRef {
  def send(sender: ActorRef, message: Message): Unit
  
  def send(message: Message)(implicit sender: ActorRef): Unit = send(sender, message)
  def :!(message: Message)(implicit sender: ActorRef): ActorRef = {
    send(sender, message)
    this
  }
}
