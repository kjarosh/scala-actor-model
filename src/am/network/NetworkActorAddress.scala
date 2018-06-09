package am.network

import java.net.SocketAddress

import am.ActorAddress

/**
 * An instance of this class identifies an actor uniquely using its network address and
 * its id.
 */
class NetworkActorAddress(val address: SocketAddress, val id: Int) extends ActorAddress {
  override def equals(any: Any): Boolean = any match {
    case adr: NetworkActorAddress =>
      adr.address.equals(address) && adr.id == id
    case _ => false
  }

  override def hashCode(): Int = address.hashCode() + id * 17

  override def toString: String = address + "@" + id
}
