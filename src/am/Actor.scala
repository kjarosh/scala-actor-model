package am

import java.net.InetSocketAddress
import java.net.SocketAddress

abstract class Actor extends Thread {
  def receive(sender: ActorRef, message: Message)

  private val rudp = new RUDPServer()

  override def run() = {
    while (!Thread.interrupted()) {
      var (address, obj) = rudp.receive()

      val sender = this.referenceAddress(address)

      obj match {
        case m: Message => receive(sender, m)
        case _ => println("Invalid message")
      }
    }
  }

  /*private def referenceAddress(address: SocketAddress) = new ActorRef {
    def send(message: Message) = {
      rudp.send(address, message)
    }
  }*/

  private def referenceAddress(address: SocketAddress): ActorRef =
    message => rudp.send(address, message)

  def reference = referenceAddress(rudp.socketAddress)
}
