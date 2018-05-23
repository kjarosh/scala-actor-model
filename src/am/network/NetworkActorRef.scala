package am.network

import am.ActorRef
import am.Message

class NetworkActorRef(
  private val server: RUDPServer,
  private val addr: ActorAddress) extends ActorRef {

  final override def send(from: ActorRef, message: Message) = {
    server.send(new MessagePacket(from = null, to = addr, contents = message))
  }
}
