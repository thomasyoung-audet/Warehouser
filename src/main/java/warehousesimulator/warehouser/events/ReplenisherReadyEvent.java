package warehousesimulator.warehouser.events;

/**
 * Represents a replenisher notifying the system that they are ready to work.
 */
public class ReplenisherReadyEvent extends EmployeeEvent {

  /**
   * Create a new ReplenisherReadyEvent.
   *
   * @param name the name of the employee involved in the event
   */
  public ReplenisherReadyEvent(String name) {
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
    return "Replenisher " + getName() + " ready";
  }
}
