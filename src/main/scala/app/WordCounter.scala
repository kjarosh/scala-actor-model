package app

import am.Actor
import am.message.Message
import am.network.NetworkActorManager


object WordCounter {

  def main(args: Array[String]): Unit = {
    //val manager = new NetworkActorManager()
    val master = new MasterActor()
    master.setName("master")

    val worker1 = new WorkerActor()
    worker1.setName("worker1")
    val worker2 = new WorkerActor()
    worker2.setName("worker2")

    //manager.register(master)

    master.addWorker(worker1.reference)
    master.addWorker(worker2.reference)

    master.reference.send(Actor.ignore, CountWordsFromFile("test.txt"))

    Thread.sleep(1000)

    //manager.shutdown()
  }
}
