package app

import am.Actor
import am.message.Message
import am.network.NetworkActorManager


class WordCounter {

  def countWordsOfFile(filename: String, workersNumber: Int): Unit ={

    val inFile = scala.io.Source.fromFile(filename)
    val lines = inFile.getLines().toArray

    val manager = new NetworkActorManager()
    val master = new Master(lines)

    manager.register(master)

    master.reference.send(Actor.ignore, StartMessage(workersNumber))

    master.reference.send(Actor.ignore, CountWordsMessage())

    Thread.sleep(1000)

    //manager.shutdown()

  }
}
