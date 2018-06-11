package app

import am.message.Message
import am.network.NetworkActorManager
import am.{AbstractActor, ActorRef}

import scala.collection.mutable

case class StartMessage(workersCount: Int) extends Message
case class CountWordsMessage() extends Message
case class CountWordsInLineMessage(line: String) extends Message
case class ResultMessage(result: mutable.HashMap[String, Int]) extends Message


class Master(lines: Array[String]) extends AbstractActor{

  val workerManager = new NetworkActorManager()
  val workersArray = Array.empty[Worker]
  val resultMap = new mutable.HashMap[String, Int]()

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case StartMessage(workersCount) =>
      println("Master received a StartMessage")
      for(i <- 0 until workersCount){
        val worker = new Worker()
        workerManager.register(worker)
        workersArray :+ worker
      }
    case CountWordsMessage() =>
      println("Master received a CountWordsMessage")
      val circular = Iterator.continually(workersArray).flatten
      for(line <- this.lines) {
        //println(line)
        circular.next().reference :! CountWordsInLineMessage(line)
      }
    case ResultMessage(result) =>
      for ((key, value) <- result) {
        resultMap.put(key, resultMap.getOrElse(key, 0)+value)
      }
  }
}
