package datastream

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.danielasfregola.twitter4s.entities.Tweet
import com.typesafe.scalalogging.Logger

import scala.collection.mutable

case class BestTweet[StatType](tweet: Tweet, stat: StatType)

class BestTweetsActor extends AbstractActor {
  private def logger = Logger("BestTweetsActor")

  private val tweets = mutable.Queue[Array[String]]()

  private var mostContrib: BestTweet[Int] = BestTweet(null, 0)
  private var mostFav: BestTweet[Int] = BestTweet(null, 0)
  private var mostRetweet: BestTweet[Long] = BestTweet(null, 0)

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case TweetMessage(tweet) =>
      if (tweet.contributors.size >= mostFav.stat) {
        mostContrib = BestTweet(tweet, tweet.contributors.size)
      }

      if (tweet.favorite_count >= mostFav.stat) {
        mostFav = BestTweet(tweet, tweet.favorite_count)
      }

      if (tweet.retweet_count >= mostFav.stat) {
        mostRetweet = BestTweet(tweet, tweet.retweet_count)
      }

    case ShowResultsMessage() =>
      System.out.synchronized {
        println("\n\n")
        println("Most favorited tweet:")
        display(mostFav)
        println()
        println("Most retweeted tweet:")
        display(mostRetweet)
        println()
        println("Most contributed tweet:")
        display(mostContrib)
      }

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }

  def display[StatType](best: BestTweet[StatType]): Unit = {
    if(best.tweet != null)
      println(best.tweet.text + " at " + best.tweet.created_at + s"(${best.stat})")
  }
}
