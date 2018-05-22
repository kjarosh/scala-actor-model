package am

@FunctionalInterface
trait ActorRef {
  def send(sender: ActorRef, message: Message)
}
