package am

import am.message.{Message, TrackIdleMessage}
import am.network.NetworkActorManager
import am.util.{DispatchMessage, Dispatcher}
import org.scalatest.FunSuite

case class DispatcherMessage() extends Message

class DispatcherReceiveSuite extends FunSuite {

  var freeTargets = 3

  class TestActor extends AbstractActor {
    override def receive(sender: ActorRef, message: Message): Unit = message match {
      case DispatcherMessage() =>
        freeTargets -= 1
    }
  }

  val dispatcherActor: AbstractActor = (sender: ActorRef, message: Message) => message match {
    case _ =>
      println("Owner of dispatcher received message")
  }
  val dispatcher = new Dispatcher(dispatcherActor.reference)

  val dispatcherManager = new NetworkActorManager()

  val actor1 = new TestActor
  val actor2 = new TestActor
  val actor3 = new TestActor

  val manager1 = new NetworkActorManager()
  val manager2 = new NetworkActorManager()
  val manager3 = new NetworkActorManager()

  manager1.register(actor1)
  manager2.register(actor2)
  manager3.register(actor3)
  dispatcherManager.register(dispatcher)

  dispatcher.add(actor1.reference)
  dispatcher.add(actor2.reference)
  dispatcher.add(actor3.reference)

  dispatcher.reference.send(Actor.ignore, DispatchMessage(DispatcherMessage()))
  dispatcher.reference.send(Actor.ignore, DispatchMessage(DispatcherMessage()))
  dispatcher.reference.send(Actor.ignore, DispatchMessage(DispatcherMessage()))

  test("Dispatcher should dispatch message to first 3 free actors"){
    assert(freeTargets == 0)
  }


}
