package app

import am.Actor
import am.message.Message
import am.network.NetworkActorManager


object WordCounter {

  def main(args: Array[String]): Unit = {
    val masterManager = new NetworkActorManager()
    val cluster1Manager = new NetworkActorManager()
    val cluster2Manager = new NetworkActorManager()

    val master = new MasterActor()
    master.setName("master")
    masterManager.register(master)

    val worker1 = new WorkerActor()
    worker1.setName("worker1")
    cluster1Manager.register(worker1)

    val worker2 = new WorkerActor()
    worker2.setName("worker2")
    cluster2Manager.register(worker2)


    master.addWorker(worker1.reference)
    master.addWorker(worker2.reference)

    master.reference.send(Actor.ignore, CountWordsFromFile("test.txt"))

    //manager.shutdown()
  }
}
