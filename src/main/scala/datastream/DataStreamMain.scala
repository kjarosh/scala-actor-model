package datastream

import am.Actor
import am.network.NetworkActorManager
import am.util.{Broadcaster, Dispatcher}
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger


object DataStreamMain {
  private val logger = Logger("DataStreamMain")

  private implicit val sender = Actor.ignore

  def main(args: Array[String]): Unit = {
    val wordStat = new WordStatisticsActor()
    val provider = new ProviderActor(wordStat.reference)

    logger.info("Loading config")
    val conf = ConfigFactory.load()

    val consumerToken = ConsumerToken(
      key = conf.getString("consumer-key"),
      secret = conf.getString("consumer-secret")
    )

    val accessToken = AccessToken(
      key = conf.getString("access-key"),
      secret = conf.getString("access-secret")
    )

    provider.reference :! StartProvidingMessage(consumerToken, accessToken)

    while(!Thread.interrupted()){
      wordStat.reference :! ShowResultsMessage()
      Thread.sleep(1000)
    }
  }
}
