package am.message

/**
 * When an actor receives this message, it will
 * respond back {@code whenIdle} when it's idle.
 */
case class TrackIdleMessage(
  val message: Message,
  val whenIdle: Message) extends Message
