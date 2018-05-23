package am.network

import am.ActorRef
import am.Message
import java.net.SocketAddress
import java.net.InetSocketAddress

class NetworkActorRef(
  private val server: RUDPServer,
  private val addr: ActorAddress) extends ActorRef {

  private def dummyAddress = new ActorAddress(new InetSocketAddress("192.0.0.8", 0), 0)

  final override def send(from: ActorRef, message: Message) = {
    val fromAddr = from match {
      case n: NetworkActorRef => n.addr
      case _ => dummyAddress
    }

    server.send(new MessagePacket(from = fromAddr, to = addr, contents = message))
  }
}
