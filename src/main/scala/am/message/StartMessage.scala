package am.message

/**
 * A generic message. When an actor receives it, it shall start its work.
 */
final case class StartMessage() extends Message
