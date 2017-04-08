package warehousesimulator.warehouser.events;

/**
 * Represents a Loader notifying the system that they are ready to work.
 */
public final class LoaderReadyEvent extends EmployeeEvent {

  /**
   * Create a new LoaderReadyEvent.
   *
   * @param name the name of the now ready loader
   */
  public LoaderReadyEvent(String name) {
    super(name);
  }

  /**
   * Have a visitor process this event.
   *
   * @param visitor The visitor to receive this event
   */
  @Override
  public void accept(EventVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "Loader " + getName() + " ready";
  }
}
