package warehousesimulator.warehouser.navigation;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import warehousesimulator.warehouser.WarehouseLocation;

/**
 * Represents a step in a {@link warehousesimulator.warehouser.Picker}s assigned picking request
 */
public class WarehouseDirection {

  private final WarehouseLocation location;
  private final String sku;

  /**
   * Create a new WarehouseDirection.
   *
   * @param location Location the picker is directed to
   * @param sku sku of the item to be retrieved from that location
   * @deprecated Use {@link WarehouseDirection#WarehouseDirection(WarehouseLocation, String)}
   *     instead
   */
  @Deprecated
  public WarehouseDirection(WarehouseLocation location, int sku) {
    this(location, Integer.toString(sku));
  }

  /**
   * Create a new WarehouseDirection.
   *
   * @param location location is the picker is directed to
   * @param sku the sku of the item to be retrieved from that location
   */
  public WarehouseDirection(WarehouseLocation location, String sku) {
    Objects.requireNonNull(sku);
    this.location = location;
    this.sku = sku;
  }

  /**
   * Get the rack space the picker is being directed to.
   *
   * @return A {@link WarehouseLocation} representing the rack space containing the item to be
   *     retrieved.
   */
  public WarehouseLocation getLocation() {
    return location;
  }

  /**
   * Get the sku to be retrieved from the location.
   *
   * @return The sku as an integer
   */
  public String getSku() {
    return sku;
  }

  /**
   * {@inheritDoc}.
   *
   * <p>Two {@link WarehouseDirection}s are equal if their location and SKU are the same.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    WarehouseDirection direction = (WarehouseDirection) obj;

    return new EqualsBuilder().append(direction.getLocation(), location)
        .append(direction.getSku(), sku).build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(location)
        .append(sku)
        .toHashCode();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String toString() {
    return location.toString() + " for sku " + sku;
  }
}
