package am.network

import java.net.DatagramSocket
import java.net.DatagramPacket
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.net.SocketAddress

class RUDPServer {
  private val socket = new DatagramSocket();

  def port = socket.getLocalPort
  def socketAddress = socket.getLocalSocketAddress

  def send(packet: MessagePacket) = {
    val bos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(bos)
    oos.writeObject(packet.contents)
    oos.close()

    val buf = bos.toByteArray()
    val dt = new DatagramPacket(buf, buf.length, packet.to.address);
    socket.send(dt);
  }

  def receive(): MessagePacket = {
    val buf = new Array[Byte](1024)
    val packet = new DatagramPacket(buf, buf.length);

    socket.receive(packet);
    
    val ois = new ObjectInputStream(new ByteArrayInputStream(buf))
    val ret = ois.readObject()
    ois.close()

    val from = new ActorAddress(packet.getSocketAddress, 0)
    val to = new ActorAddress(this.socketAddress, 0)
    return new MessagePacket(from = from, to = to, contents = ret)
  }
}

