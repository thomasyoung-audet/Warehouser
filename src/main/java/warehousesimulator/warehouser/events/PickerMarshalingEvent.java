package warehousesimulator.warehouser.events;

/**
 * Represents a picker notifying the system that they have arrived at marshalling.
 */
public final class PickerMarshalingEvent extends EmployeeEvent {

  /**
   * Create a new PickerMarshalingEvent.
   *
   * @param name the name of the picker arriving at marshaling
   */
  public PickerMarshalingEvent(String name) {
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
    return  "Picker " + getName() + " marshaling";
  }
}
