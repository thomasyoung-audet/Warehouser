package warehousesimulator.warehouser.events;

import java.util.Objects;

/**
 * Represents a sequencer scanning the 1 of the 8 skus of two pallets worth of orders, first the
 *     front four followed by the back four to check if the package matches the expected orders.
 */
public final class SequencerSequenceEvent extends EmployeeEvent {

  private final String sku;

  /**
   * Create a new SequencerSequenceEvent
   *
   * @param name the name of the employee involved in the event
   * @param sku the SKU being scanned.
   */
  public SequencerSequenceEvent(String name, String sku) {
    super(name);
    Objects.requireNonNull(sku);
    this.sku = sku;
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

  /**
   * Get this <code>SequencerSequenceEvent</code>'s sku.
   *
   * @return the SKU currently being scanned.
   */
  public String getSku() {
    return sku;
  }

  @Override
  public String toString() {
    return String.format("Sequencer %1$s sequences %2$s",
        getName(), sku);
  }
}
