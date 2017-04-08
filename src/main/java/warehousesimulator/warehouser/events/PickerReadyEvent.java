package warehousesimulator.warehouser.events;

/**
 * Represents a picker notifying the system that they are ready to work.
 */
public final class PickerReadyEvent extends EmployeeEvent {

  /**
   * Create a new PickerReadyEvent.
   *
   * @param name the name of the now picker
   */
  public PickerReadyEvent(String name) {
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

  public String toString() {
    return "Picker " + getName() + " ready";
  }
}
