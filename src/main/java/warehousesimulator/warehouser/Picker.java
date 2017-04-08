package warehousesimulator.warehouser;

import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An employee representing a warehouse picker tasked with fulfilling picking requests.
 * 
 * <p>Pickers are given a {@link PickingRequest} to execute.
 */
public class Picker extends Employee {

  /** The current picking request. */
  private PickingRequest task;

  /**
   * Create a new {@link Picker} with the given name.
   * 
   * @param name the name of the {@link Picker}.
   */
  public Picker(String name) {
    super(name);
  }

  /**
   * {@inheritDoc}.
   * 
   * <p>The picker is ready if it does not have an active picking request.
   */
  @Override
  public boolean isReady() {
    return task == null;
  }

  /**
   * Get the picker's current picking request.
   * 
   * @return The picker's current task/picking request.
   */
  public Optional<PickingRequest> getTask() {
    return Optional.ofNullable(task);
  }

  /**
   * Set the task of the picker.
   * 
   * @param task the new task of the picker. This can be <code>null</code> if the picker should no
   *        longer have a task.
   */
  public void setTask(PickingRequest task) {
    this.task = task;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(25, 9)
        .append(getName())
        .build();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    if (obj.getClass() != getClass()) {
      return false;
    }

    Picker other = (Picker) obj;
    return new EqualsBuilder()
        .append(getName(), other.getName())
        .build();
  }
}
