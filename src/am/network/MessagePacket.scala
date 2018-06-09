package am.network

import am.ActorAddress

case class MessagePacket(
  from: ActorAddress,
  to: NetworkActorAddress,
  contents: Object)
