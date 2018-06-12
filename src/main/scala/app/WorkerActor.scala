package app

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable


class WorkerActor extends AbstractActor {
  private def logger = WorkerActor.logger

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case CountWordsInLineMessage(line) =>
      val result: mutable.HashMap[String, Int] = mutable.HashMap.empty[String, Int]

      for (word <- line.split("[ “”\",!.()/]+")) {
        val wordl = word.toLowerCase.trim
        if (!wordl.isEmpty)
          result(wordl) = result.getOrElse(wordl, 0) + 1
      }

      logger.info("Counted '" + line + "' to " + result.toMap)
      sender :! CountedWords(result.toMap)

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}

object WorkerActor {
  private val logger = Logger("Worker")
}
