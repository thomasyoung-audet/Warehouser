package warehousesimulator.warehouser;

import java.util.Objects;

/**
 * A named employee.
 */
public abstract class Employee {

  /** The name of this employee. */
  private final String name;

  /**
   * Create a new {@link Employee} with the given name.
   * 
   * @param name the name of the employee.
   */
  public Employee(String name) {
    Objects.requireNonNull(name);
    this.name = name;
  }

  /**
   * Get the name of this {@link Employee}.
   * 
   * @return the name of the employee.
   */
  public String getName() {
    return name;
  }

  /** Indicates whether this employee is ready or not. */
  public abstract boolean isReady();
}
