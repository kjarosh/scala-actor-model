package app

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable


class WorkerActor extends AbstractActor {
  private def logger = WorkerActor.logger

  val result: mutable.HashMap[String, Int] = mutable.HashMap.empty[String, Int]

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case CountWordsInLineMessage(line) =>
      for (word <- line.split("[ ,!.]+")) {
        val wordl = word.toLowerCase
        result(wordl) = result.getOrElse(wordl, 0) + 1
      }
      sender :! CountedWords(result.toMap)

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}

object WorkerActor {
  private val logger = Logger("Worker")
}
