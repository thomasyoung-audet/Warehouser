package warehousesimulator.warehouser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An employee tasked with loading pallets onto a truck for shipping.
 */
public class Loader extends Employee {

  /** The PickingRequest this Loader is currently scanning. */
  private PickingRequest currentPickingRequest;

  /** The index of the last fascia that was scanning. */
  private int currentScanningIndex;

  /**
   * Create a new {@link Loader} with the given name.
   *
   * @param name the name of the loader
   */
  public Loader(String name) {
    super(name);
  }

  @Override
  public boolean isReady() {
    return true;
  }


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

    Loader other = (Loader) obj;
    return new EqualsBuilder()
        .append(getName(), other.getName()).build();
  }


  @Override
  public int hashCode() {
    return new HashCodeBuilder(79, 19)
        .append(getName())
        .build();
  }
}
