package am.network

import java.net.SocketAddress

class ActorAddress(val address: SocketAddress, val id: Int) extends Serializable {
  override def equals(any: Any): Boolean = any match {
    case adr: ActorAddress =>
      adr.address.equals(address) && adr.id == id
    case _ => false
  }

  override def hashCode() = address.hashCode() + id * 17
  override def toString() = address + "@" + id
}
