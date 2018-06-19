package datastream

import am.message.Message
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Tweet}

case class ShowResultsMessage() extends Message

case class StartProvidingMessage(
  consumerToken: ConsumerToken,
  accessToken: AccessToken) extends Message

case class TweetMessage(
  tweet: Tweet) extends Message
