package am.network

import am._
import am.message.{KillMessage, Message}

import scala.collection.mutable

/**
 * This class is responsible for managing multiple actors.
 * It also provides a network interface to communicate between
 * actors across the network.
 *
 * The manager itself runs in its own thread.
 */
class NetworkActorManager extends ActorManager {
  /**
   * Used for sending and receiving messages.
   */
  private val server = new UDPServer()
  private val registered = new mutable.HashMap[Int, Actor]()
  private var nextId = 0

  private val thread = new Thread(
    () => while (!Thread.interrupted()) handlePacket())
  thread.start()

  /**
   * The given actor is registered in this manager and is given a unique
   * network address.
   */
  override def register(actor: Actor): Unit = {
    val id = nextId
    nextId += 1

    registered.put(id, actor)
    actor.reference = referenceAddress(new NetworkActorAddress(server.socketAddress, id))

    actor.registered(this)
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
      case m: Message => receiver.dispatch(sender, m)
      case _ => println("Invalid message")
    }
  }

  override def referenceAddress(addr: ActorAddress): ActorRef = {
    val noAddress = ActorAddress.noAddress
    addr match {
      case `noAddress` => Actor.ignore
      case naddr: NetworkActorAddress => new NetworkActorRef(server, naddr)
      case _ => throw UnsupportedAddressExeption(addr)
    }
  }

  override def shutdown(): Unit = {
    // stop listening to
    thread.interrupt()
    registered.foreach({
      case (_, actor) => actor.dispatch(Actor.ignore, KillMessage())
    })
    server.close()
  }
}
