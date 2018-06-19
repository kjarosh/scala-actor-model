package datastream

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable

class WordStatisticsActor extends AbstractActor {
  private def logger = Logger("WordStatisticsActor")

  private val tweets = mutable.Queue[Array[String]]()

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case TweetMessage(tweet) =>
      val words = tweet.text
        .split("\\s+")
        .map(s => WordStatisticsActor.toAlnum(s))
        .map(s => s.toLowerCase)
        .filter(p => p.length > 4)

      if (tweets.size >= 5000) tweets.dequeue()
      tweets.enqueue(words)

    case ShowResultsMessage() =>
      val sorted = tweets
        .flatten
        .groupBy(identity)
        .mapValues(_.size)
        .toSeq
        .sortWith((a, b) => a._2 > b._2)

      System.out.synchronized {
        println("\n\n")

        for (i <- 10 to 0 by -1) {
          if (i < sorted.size)
            println(s"${sorted(i)._2} | ${sorted(i)._1}")
        }

        println(s"Those were 10 most popular words")
        println(s"Total tweets: ${tweets.size}")
      }

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}

object WordStatisticsActor {
  private def toAlnum(s: String): String = {
    new String(s.toCharArray().filter(s => Character.isLetterOrDigit(s)))
  }
}
