package app

import java.nio.file.{Files, Paths}

import am.message.Message
import am.util.{DispatchMessage, Dispatcher}
import am.{AbstractActor, ActorRef}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable


class MasterActor() extends AbstractActor {
  private def logger = MasterActor.logger

  val dispatcher = new Dispatcher(reference)
  addSibling(dispatcher)
  val dispatcherRef = dispatcher.reference

  val results = new mutable.HashMap[String, Int]()

  def addWorker(actor: ActorRef): Unit = {
    dispatcher.add(actor)
  }

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case CountWordsFromFile(filename) =>
      logger.info(s"Received a request to count file: $filename")
      Files.lines(Paths.get(filename))
        .map[Message](CountWordsInLineMessage(_))
        .map[Message](DispatchMessage(_))
        .forEach(dispatcherRef.send(_))

    case CountedWords(result) =>
      for ((key, value) <- result) {
        results.put(key, results.getOrElse(key, 0) + value)
      }
      logger.info("Results so far: " + results)

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}

object MasterActor {
  private val logger = Logger("Master")
}
