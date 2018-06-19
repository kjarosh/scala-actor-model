package datastream

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable

class CountryStatisticsActor extends AbstractActor {
  private def logger = Logger("CountryStatisticsActor")

  private val countries = mutable.SortedMap[String, Long]()

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case TweetMessage(tweet) =>
      val country: String = tweet.place.map(_.country).orNull

      if (country != null) {
        countries.put(country, countries.getOrElse(country, 0L) + 1L)
      }

    case ShowResultsMessage() =>
      System.out.synchronized {
        println("\n\n")

        for ((country, count) <- countries.toSeq.sortWith(_._2 < _._2)) {
          println(s"${count} | ${country}")
        }

        println(s"Those were most popular countries")
      }

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}


