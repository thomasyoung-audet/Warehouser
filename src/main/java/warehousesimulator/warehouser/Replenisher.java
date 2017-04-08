package warehousesimulator.warehouser;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An employee tasked with restocking warehouse racks.
 */
public class Replenisher extends Employee {
  private ReplenishmentRequest task;

  /**
   * Create a new {@link Replenisher} with the given name.
   * 
   * @param name the name of the replenisher.
   */
  public Replenisher(String name) {
    super(name);
  }

  public void setTask(ReplenishmentRequest task) {
    this.task = task;
  }

  public Optional<ReplenishmentRequest> getTask() {
    return Optional.ofNullable(task);
  }

  @Override
  public boolean isReady() {
    return task == null;
  }


  /**
   * {@inheritDoc}.
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(127, 33)
        .append(getName())
        .build();
  }

  /**
   * {@inheritDoc}.
   * 
   * <p>Two Replenishers are the same if they have the same name.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (obj == this) {
      return true;
    }

    if (obj.getClass() != getClass()) {
      return false;
    }

    Replenisher other = (Replenisher) obj;

    return new EqualsBuilder()
        .append(getName(), other.getName())
        .build();

  }
}
