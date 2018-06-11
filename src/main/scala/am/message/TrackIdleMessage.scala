package am.message

/**
 * When an actor receives this message, it will respond back
 * [[whenIdle]] when it's idle, and [[message]] will be handled normally.
 */
final case class TrackIdleMessage(
  message: Message,
  whenIdle: Message) extends Message
