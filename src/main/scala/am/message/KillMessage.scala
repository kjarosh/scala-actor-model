package am.message

/**
 * When an actor receives this message, it shuts down.
 */
final case class KillMessage() extends Message
