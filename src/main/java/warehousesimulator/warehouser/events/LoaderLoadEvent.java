package warehousesimulator.warehouser.events;

/**
 * Represents a Loader notifying the warehouse management system that they have loaded a set of
 * pallets for shipping.
 */
public class LoaderLoadEvent extends EmployeeEvent {

  /**
   * Create a new LoaderLoadEvent.
   *
   * @param name the name of the employee involved in the event
   */
  public LoaderLoadEvent(String name) {
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
    return "Loader " + getName() + " loads";
  }
}
