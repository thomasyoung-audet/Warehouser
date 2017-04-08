package warehousesimulator.warehouser.events;

/**
 * Represents a loader notifying the system that they are rescanning the skus.
 */
public class LoaderRescanEvent extends EmployeeEvent {

  /**
   * Create a new LoaderRescanEvent.
   *
   * @param name the name of the sequencer rescanning
   */
  public LoaderRescanEvent(String name) {
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
    return "Loader " + getName() + " rescanning";
  }
}
