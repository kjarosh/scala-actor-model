package am

import am.message.Message
import am.network.NetworkActorManager

case class StringMessage(s: String) extends Message

case class SendMessage(s: String) extends Message

object Main {
  def main(args: Array[String]): Unit = {
    val actor1 = new AbstractActor {
      override def receive(sender: ActorRef, message: Message): Unit = message match {
        case StringMessage(s) => println("Received string: " + s)
        case _ => println("Unrecognized message 1")
      }
    }
    val actorManager1 = new NetworkActorManager()
    actorManager1.register(actor1)

    val actor2: AbstractActor = new AbstractActor {
      val actor1ref: ActorRef = actor1.reference

      override def receive(sender: ActorRef, message: Message): Unit = message match {
        case SendMessage(s) =>
          actor1ref :! StringMessage(s)
          println("Passed message")
        case _ => println("Unrecognized message 2")
      }
    }
    val actorManager2 = new NetworkActorManager()
    actorManager2.register(actor2)

    actor2.reference.send(Actor.ignore, SendMessage("test"))

    Thread.sleep(1000)

    /*actor1.reference.send(Actor.ignore, KillMessage())
    actor2.reference.send(Actor.ignore, KillMessage())*/

    actorManager1.shutdown()
    actorManager2.shutdown()
  }
}
