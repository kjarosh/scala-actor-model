package am.message

/**
 * When an actor receives this message, it shuts down completely.
 */
case class KillMessage() extends Message
