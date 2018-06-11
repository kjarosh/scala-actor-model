package am

/**
 * An exception thrown when an [[am.ActorAddress]] is unsupported by a routine.
 */
final case class UnsupportedAddressExeption(
  private val addr: ActorAddress = null,
  private val cause: Throwable = None.orNull)
  extends Exception("address: " + addr, cause)
