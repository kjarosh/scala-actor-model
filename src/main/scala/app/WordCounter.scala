package app

import am.Actor
import am.network.NetworkActorManager


object WordCounter {
  def main(args: Array[String]): Unit = {
    val enableNetworking = true
    val masterManager = new NetworkActorManager("127.0.0.1")
    val cluster1Manager = new NetworkActorManager("127.0.0.1")
    val cluster2Manager = new NetworkActorManager("127.0.0.1")

    val master = new MasterActor()
    master.setName("master")
    if (enableNetworking) masterManager.register(master)

    val worker1 = new WorkerActor()
    worker1.setName("worker1")
    if (enableNetworking) cluster1Manager.register(worker1)

    val worker2 = new WorkerActor()
    worker2.setName("worker2")
    if (enableNetworking) cluster2Manager.register(worker2)

    val worker3 = new WorkerActor()
    worker3.setName("worker2")
    cluster3Manager.register(worker3)


    master.addWorker(worker1.reference)
    master.addWorker(worker2.reference)
    master.addWorker(worker3.reference)

    master.reference.send(Actor.ignore, CountWordsFromFileMessage("test.txt"))
  }
}
