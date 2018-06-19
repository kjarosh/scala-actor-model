package am

import org.scalatest.{FlatSpec, FunSuite}
import am.message.Message
import am.network.NetworkActorManager

case class ExampleMessage(s: String) extends Message
case class OtherMessage() extends Message

class MessageReceiveSuite extends FunSuite {

  var msg: String = _

  class TestActor extends AbstractActor {
    var receivedExample = 0
    var receivedOther = 0

    var storedMessage: String = _

    override def receive(sender: ActorRef, message: Message): Unit = message match {
      case ExampleMessage(s) =>
        test("Received message should be 'Hello'") {
          assert(s == "Hello")
        }
      case _ =>
        test("Received message should have OtherMessage type") {
          assert(message.isInstanceOf[OtherMessage])
        }
    }
  }

  val actor = new TestActor()

  val manager = new NetworkActorManager()

  manager.register(actor)
  actor.reference.send(Actor.ignore, ExampleMessage("Hello"))
  actor.reference.send(Actor.ignore, OtherMessage())

}

