package am

import am.message.Message
import am.network.NetworkActorManager
import am.util.Broadcaster
import org.scalatest.FunSuite

case class BroadcasterMessage() extends Message

class BroadcasterReceiveSuite extends FunSuite{

  var count = 3


  class TestActor extends AbstractActor {
    override def receive(sender: ActorRef, message: Message): Unit = message match {
      case BroadcasterMessage() =>
        count -= 1
    }
  }

  val broadcaster = new Broadcaster()

  val actor1 = new TestActor
  val actor2 = new TestActor
  val actor3 = new TestActor

  val manager1 = new NetworkActorManager()
  val manager2 = new NetworkActorManager()
  val manager3 = new NetworkActorManager()
  val broadcasterManager = new NetworkActorManager()

  manager1.register(actor1)
  manager2.register(actor2)
  manager3.register(actor3)
  broadcasterManager.register(broadcaster)

  broadcaster.add(actor1.reference)
  broadcaster.add(actor2.reference)
  broadcaster.add(actor3.reference)

  broadcaster.reference.send(Actor.ignore, BroadcasterMessage())
  test("After 3 actors received message from broadcaster, count variable value should decrease to 0"){
    assert(count == 0)
  }
}
