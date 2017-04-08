package warehousesimulator.warehouser.events;

/**
 * Represents a replenisher restocking a rack space.
 */
public class ReplenisherReplenishEvent extends EmployeeEvent {

  private final String location;

  /**
   * Create a new <code>ReplenisherReplenishEvent</code>.
   *
   * @param name the name of the employee involved in the event
   */
  public ReplenisherReplenishEvent(String name, String location) {
    super(name);
    this.location = location;
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

  public String getLocation() {
    return location;
  }

  @Override
  public String toString() {
    return "Replenisher " + getName() + " replenish " + location;
  }
}
