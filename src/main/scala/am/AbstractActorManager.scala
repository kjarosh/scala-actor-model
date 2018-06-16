package am

abstract class AbstractActorManager extends ActorManager {
  override def referenceAddress(address: ActorAddress): ActorRef = {
    val noAddress = ActorAddress.noAddress
    address match {
      case `noAddress` => Actor.ignore
      case _ => throw UnsupportedAddressExeption(address)
    }
  }

  override def shutdown(): Unit = {}
}
