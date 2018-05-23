package am.message

/**
 * When an actor receives this message, it will respond back
 * with {@code confirmation}.
 */
case class TrackMessage(
  val message: Message,
  val confirmation: Message) extends Message
