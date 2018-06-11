package am

import am.message.Message

/**
 * This is a portable actor reference, i.e. such that it can be passed to a
 * different program or machine and still be fully effective.
 */
@FunctionalInterface
trait PortableActorRef extends ActorRef {
  def send(sender: ActorRef, message: Message): Unit = sender match {
    case portable: PortableActorRef => send(portable, message)
    case _ => throw null
  }

  def address: ActorAddress
  def send(sender: PortableActorRef, message: Message): Unit

  override def toString: String = s"address:$address"
}
