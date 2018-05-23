package am.network

import java.net.DatagramSocket
import java.net.DatagramPacket
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.net.SocketAddress
import scala.annotation.tailrec
import am.Logger

/**
 * An implementation of the Reliable UDP communication,
 * i.e. such that no packet loss is guaranteed.
 *
 * <table>
 *     <tr><th>Field</th> <th>Type</th></tr>
 *     <tr><td>Sender address</td> <td>ActorAddress</td></tr>
 *     <tr><td>Receiver address</td> <td>ActorAddress</td></tr>
 *     <tr><td>Message</td> <td>Serializable</td></tr>
 *     <caption>Packet structure</caption>
 * </table>
 */
class RUDPServer {
  private def logger = RUDPServer.logger
  private val socket = new DatagramSocket();

  /**
   * Local port.
   */
  def port = socket.getLocalPort

  /**
   * Local socket address.
   */
  def socketAddress = socket.getLocalSocketAddress

  /**
   * Send the packet.
   */
  final def send(packet: MessagePacket) = {
    logger.debug(s"Sending a packet from ${packet.from} to ${packet.to}")

    val bos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(packet.from)
    oos.writeObject(packet.to)
    oos.writeObject(packet.contents)
    oos.close()

    val buf = bos.toByteArray()
    val dt = new DatagramPacket(buf, buf.length, packet.to.address);
    socket.send(dt);
  }

  @tailrec
  final def receive(): MessagePacket = {
    val buf = new Array[Byte](1024)
    val packet = new DatagramPacket(buf, buf.length);

    socket.receive(packet);

    val ois = new ObjectInputStream(new ByteArrayInputStream(buf))
    val sender = ois.readObject()
    val receiver = ois.readObject()
    val message = ois.readObject()
    ois.close()

    logger.debug(s"Received a packet from $sender to $receiver")

    return (sender, receiver) match {
      case (from: ActorAddress, to: ActorAddress) =>
        new MessagePacket(from = from, to = to, contents = message)
      case _ => {
        logger.err("Invalid from/to")
        receive()
      }
    }
  }
}

object RUDPServer {
  private val logger = new Logger("RUDPServer")
}
