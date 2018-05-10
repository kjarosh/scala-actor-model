package am

@FunctionalInterface
trait ActorRef {
  def send(message: Message)
}
