package am.network

case class MessageTooLongException(
  length: Int,
  cause: Throwable = null)
  extends Exception("length: " + length, cause)
