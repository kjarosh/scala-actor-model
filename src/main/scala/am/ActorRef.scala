package am

import am.message.Message

/**
 * Reference to an actor. It does not uniquely identify an actor, instead it's a shortcut for
 * sending messages to actors.
 */
@FunctionalInterface
trait ActorRef {
  /**
   * Send a message to the actor referenced by this reference specyfing a sender.
   *
   * @param sender  the sender of the message
   * @param message the message to send
   */
  def send(sender: ActorRef, message: Message): Unit

  /**
   * Send a message to the actor referenced by this reference.
   *
   * @param message the message to send
   * @param sender  the sender of the message
   */
  def send(message: Message)(implicit sender: ActorRef): Unit = send(sender, message)

  /**
   * Send a message to the actor referenced by this reference.
   *
   * @param message the message to send
   * @param sender  the sender of the message
   * @return sent message
   */
  def :!(message: Message)(implicit sender: ActorRef): Message = {
    send(sender, message)
    message
  }
}
