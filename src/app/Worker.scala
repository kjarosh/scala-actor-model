package app

import am.message.Message
import am.{AbstractActor, ActorRef}

import scala.collection.mutable


class Worker extends AbstractActor{
  val result: mutable.HashMap[String, Int] = mutable.HashMap.empty[String, Int]

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case CountWordsInLineMessage(line) =>
      println("Worker received a message")
      for (word <- line.split("[ ,!.]+")){
        result(word.toLowerCase) += 1
      }
      sender :! ResultMessage(result)
  }
}
