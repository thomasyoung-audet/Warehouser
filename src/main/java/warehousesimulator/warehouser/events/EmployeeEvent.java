package warehousesimulator.warehouser.events;

import java.util.Objects;

/**
 * An event involving a named employee.
 */
public abstract class EmployeeEvent implements Event {

  private final String name;


  /**
   * Create a new EmployeeEvent.
   *
   * @param name the name of the employee involved in this event
   */
  public EmployeeEvent(String name) {
    Objects.requireNonNull(name);
    this.name = name;
  }

  /**
   * Get the name of the employee involved in this event.
   *
   * @return the name of the employee involved in this event
   */
  public String getName() {
    return name;
  }
}
