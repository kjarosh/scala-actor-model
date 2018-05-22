package am

import am.network.RUDPServer
import scala.collection.mutable.HashMap
import am.network.ActorAddress
import am.network.MessagePacket

/**
 * This class is responsible for managing multiple actors.
 * It also provides a network interface to communicate between
 * actors across the network.
 *
 * The manager itself runs in its own thread.
 */
class ActorManager {
  /**
   * Used for sending and receiving messages.
   */
  private val server = new RUDPServer()
  private val registered = new HashMap[Int, Actor]()

  private val thread = new Thread(
    () => while (!Thread.interrupted()) handlePacket())
  thread.start()

  /**
   * The given actor is registered in this manager and given a unique
   * network address.
   */
  def register(actor: Actor) = {

  }

  private def handlePacket(): Unit = {
    val packet = server.receive()

    val receiver = registered.get(packet.to.id).orNull
    if (receiver == null) {
      println("Invalid receiver")
      return
    }

    val sender = referenceAddress(packet.from)

    packet.contents match {
      case m: Message => receiver.receive(sender, m)
      case _ => println("Invalid message")
    }
  }

  private def referenceAddress(addr: ActorAddress): ActorRef = (sender, message) =>
    server.send(new MessagePacket(from = null, to = addr, contents = message))
}
