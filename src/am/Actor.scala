package am

import java.net.InetSocketAddress
import java.net.SocketAddress
import am.network.RUDPServer
import am.network.ActorAddress
import am.network.MessagePacket

abstract class Actor extends Thread {
  def receive(sender: ActorRef, message: Message)

  private val rudp = new RUDPServer()
  val address = new ActorAddress(rudp.socketAddress, 0)

  override def run() = {
    while (!Thread.interrupted()) {
      val packet = rudp.receive()

      val sender = this.referenceAddress(packet.from)

      packet.contents match {
        case m: Message => receive(sender, m)
        case _ => println("Invalid message")
      }
    }
  }

  private def referenceAddress(address: ActorAddress): ActorRef =
    message => rudp.send(new MessagePacket(from = null, to = address, contents = message))

  def reference = referenceAddress(this.address)

}
