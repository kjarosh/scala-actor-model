package am

import am.message.Message

trait Actor {
  /**
   * Dispatch a message. The message should not be processed instantly, instead
   * it should be enqueued to handle concurrently.
   */
  def dispatch(sender: ActorRef, message: Message)

  def reference: ActorRef

  def reference_=(ref: ActorRef)
}

object Actor {
  def ignore: PortableActorRef = new PortableActorRef {
    override def address: ActorAddress = ActorAddress.noAddress

    override def send(sender: PortableActorRef, message: Message): Unit = {}
  }
}
