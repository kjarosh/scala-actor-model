package am.message

/**
 * When an actor receives this message, it will respond back
 * with [[confirmation]] and [[message]] will be handled normally.
 */
case class TrackMessage(
  message: Message,
  confirmation: Message) extends Message
