package am

@FunctionalInterface
trait ActorRef {
  def send(message: Message)(implicit sender: ActorRef): Unit = send(sender, message)
  
  def send(sender: ActorRef, message: Message): Unit
}
