package warehousesimulator.warehouser;

import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A request to replenish the stock of a single warehouse location.
 */
public class ReplenishmentRequest {

  private final WarehouseLocation location;
  private Replenisher assignee;

  /**
   * Create a new <code>ReplenishmentRequest</code> for the given location.
   *
   * @param location the location to be replenished
   */
  public ReplenishmentRequest(WarehouseLocation location) {
    Objects.requireNonNull(location);
    this.location = location;
  }

  /**
   * Gets the location to be replenished.
   *
   * @return a <code>WarehouseLocation</code> corresponding to the rack space to be replenished
   */
  public WarehouseLocation getLocation() {
    return location;
  }

  /**
   * Gets the replenisher assigned to this request.
   *
   * @return the {@link Replenisher} an Empty if none if this request is unassigned.
   */
  public Optional<Replenisher> getAssignee() {
    return Optional.ofNullable(assignee);
  }

  /**
   * Note that a replenisher has been assigned to this request.
   *
   * @param assignee The replenisher assigned to this request.
   */
  public void setAssignee(Replenisher assignee) {
    this.assignee = assignee;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    ReplenishmentRequest request = (ReplenishmentRequest) obj;

    return new EqualsBuilder()
        .append(location, request.location)
        .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(location)
        .toHashCode();
  }
}
