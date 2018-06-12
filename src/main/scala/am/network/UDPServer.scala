package am.network

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.net.{DatagramPacket, DatagramSocket, SocketAddress}

import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec

class UDPServer extends AutoCloseable {
  private def logger = UDPServer.logger

  private val socket = new DatagramSocket()

  /**
   * Local port.
   */
  def port: Int = socket.getLocalPort

  /**
   * Local socket address.
   */
  def socketAddress: SocketAddress = socket.getLocalSocketAddress

  /**
   * Send the packet.
   */
  final def send(packet: MessagePacket): Unit = {
    logger.debug(s"Sending a packet from ${packet.from} to ${packet.to}")

    val bos = new ByteArrayOutputStream()
    new ObjectOutputStream(bos).writeObject(packet)

    val buf = bos.toByteArray

    if (buf.length > UDPServer.MAX_PACKET_SIZE) {
      throw MessageTooLongException(buf.length)
    }

    socket.send(new DatagramPacket(buf, buf.length, packet.to.address))
  }

  private val buffer = new Array[Byte](UDPServer.MAX_PACKET_SIZE)

  @tailrec
  final def receive(): MessagePacket = {
    val dt = new DatagramPacket(buffer, buffer.length)

    socket.receive(dt)
    logger.trace("Received a datagram")

    val ois = new ObjectInputStream(new ByteArrayInputStream(dt.getData))
    val obj = ois.readObject()

    obj match {
      case packet: MessagePacket => packet
      case _ =>
        logger.error("The received packet was ill-formed")
        receive()
    }
  }

  def close(): Unit = {
    logger.trace("Closing the RUDP server")
    socket.close()
  }
}

object UDPServer {
  private val logger = Logger("UDPServer")

  // 16 KiB
  private val MAX_PACKET_SIZE = 16 * 1024
}
