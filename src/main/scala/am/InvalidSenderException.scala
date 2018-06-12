package am

final case class InvalidSenderException(
  private val message: String,
  private val cause: Throwable = None.orNull)
  extends Exception(message, cause)
