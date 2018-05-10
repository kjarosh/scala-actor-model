package am

import java.net.DatagramSocket
import java.net.InetAddress
import java.io.OutputStream
import java.net.DatagramPacket
import java.io.ObjectOutputStream
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.io.Serializable
import java.net.SocketAddress

class RUDPServer {
  private val socket = new DatagramSocket();

  def port = socket.getLocalPort
  def socketAddress = socket.getLocalSocketAddress

  def send(address: SocketAddress, obj: Serializable) = {
    var bos = new ByteArrayOutputStream()
    var oos = new ObjectOutputStream(bos)
    oos.writeObject(obj)
    oos.close()

    var buf = bos.toByteArray()
    var packet = new DatagramPacket(buf, buf.length, address);
    socket.send(packet);
  }

  def receive(): (SocketAddress, Object) = {
    var buf = new Array[Byte](1024)
    var packet = new DatagramPacket(buf, buf.length);

    socket.receive(packet);

    var bis = new ByteArrayInputStream(buf)
    var ois = new ObjectInputStream(bis)
    var ret = ois.readObject()
    ois.close()

    return (packet.getSocketAddress, ret)
  }
}

