package am

case class StringMessage(s: String) extends Message
case class SendMessage(s: String) extends Message

object Main {
  def main(args: Array[String]): Unit = {
    val actor1 = new Actor {
      override def receive(sender: ActorRef, message: Message) = message match {
        case StringMessage(s) => println("Received string: " + s)
        case _ => println("Unrecognized message 1")
      }
    }

    val actor2 = new Actor {
      val actor1ref = actor1.reference

      override def receive(sender: ActorRef, message: Message) = message match {
        case SendMessage(s) => {
          actor1ref.send(reference, new StringMessage(s))
          println("Passed message")
        }
        case _ => println("Unrecognized message 2")
      }
    }

    actor2.reference.send(Actor.ignore, new SendMessage("test"))
  }
}
