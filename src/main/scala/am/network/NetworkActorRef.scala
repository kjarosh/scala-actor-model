package am.network

import am.PortableActorRef
import am.message.Message

class NetworkActorRef(
  private val server: UDPServer,
  private val addr: NetworkActorAddress) extends PortableActorRef {

  def address: NetworkActorAddress = addr

  final override def send(from: PortableActorRef, message: Message): Unit = {
    server.send(MessagePacket(from = from.address, to = addr, contents = message))
  }
}
