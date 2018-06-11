package app

import am.AbstractActor



object MapReduce{
  def main(args: Array[String]): Unit = {
    val wordCounter = new WordCounter()

    val numberOfWorkers = 4

    val filename = args(0)
    wordCounter.countWordsOfFile(filename, numberOfWorkers)

    Thread.sleep(1000)
  }
}
