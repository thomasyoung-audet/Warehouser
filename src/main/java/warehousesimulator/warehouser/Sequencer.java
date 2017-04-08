package warehousesimulator.warehouser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A sequencer tasked with checking that the fascia in sets of pallets match up with their orders.
 */
public class Sequencer extends Employee {

  /** The PickingRequest this Sequencer is currently sequencing. */
  private PickingRequest currentPickingRequest;

  /** The index of the last fascia that was sequenced. */
  private int currentSequencingIndex;

  /**
   * Create a new {@link Sequencer} with the given name.
   *
   * @param name the name of the sequencer.
   */
  public Sequencer(String name) {
    super(name);
  }


  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(45, 55).append(getName()).build();
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
    Sequencer other = (Sequencer) obj;
    return new EqualsBuilder().append(getName(), (other).getName()).build();
  }
}
