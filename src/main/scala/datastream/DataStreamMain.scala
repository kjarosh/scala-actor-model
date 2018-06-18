package datastream

import am.Actor
import am.network.NetworkActorManager
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken}
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

object DataStreamMain {
  private val logger = Logger("DataStreamMain")

  def main(args: Array[String]): Unit = {
    val enableNetworking = true

    val providersManager = new NetworkActorManager("127.0.0.1")
    val realtimeManager = new NetworkActorManager("127.0.0.1")

    val provider = new ProviderActor(Actor.ignore)

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

    provider.reference.send(Actor.ignore, StartProvidingMessage(consumerToken, accessToken))
  }
}
