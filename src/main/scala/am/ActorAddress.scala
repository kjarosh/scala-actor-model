package am

/**
 * An address uniquely identifying an actor.
 */
trait ActorAddress extends Serializable {

}

case class NoActorAddress() extends ActorAddress

object ActorAddress {
  def noAddress: ActorAddress = NoActorAddress()
}
