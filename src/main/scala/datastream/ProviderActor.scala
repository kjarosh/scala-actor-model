package datastream

import am.message.Message
import am.{AbstractActor, ActorRef}
import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Tweet}
import com.danielasfregola.twitter4s.{TwitterRestClient, TwitterStreamingClient}
import com.typesafe.scalalogging.Logger

class ProviderActor(private val target: ActorRef) extends AbstractActor {
  private def logger = Logger("ProviderActor")

  override def receive(sender: ActorRef, message: Message): Unit = message match {
    case StartProvidingMessage(consumerToken, accessToken) =>
      logger.info(s"Starting work")

      val restClient = TwitterRestClient(consumerToken, accessToken)
      val streamingClient = TwitterStreamingClient(consumerToken, accessToken)

      def printTweetText: PartialFunction[StreamingMessage, Unit] = {
        case tweet: Tweet => target ! TweetMessage(tweet)
      }

      streamingClient.sampleStatuses(stall_warnings = true)(printTweetText)

    case _ =>
      logger.error(s"Unrecognized message: $message from $sender")
  }
}
