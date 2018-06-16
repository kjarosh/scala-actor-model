package am

trait ActorManager {
  /**
   * This method is used for registering actors.
   *
   * When an actor is registered, a unique [[ActorAddress]] is associated with it.
   */
  def register(actor: Actor)

  /**
   * This method is using for referencing addresses that are asociated with actors.
   *
   * @param address actor's address
   * @return reference to an actor associated with the given address
   */
  def referenceAddress(address: ActorAddress): ActorRef

  def shutdown()
}
