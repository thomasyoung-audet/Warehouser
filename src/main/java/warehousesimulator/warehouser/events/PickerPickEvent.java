package warehousesimulator.warehouser.events;

import java.util.Objects;

/**
 * Represents a picker scanning the barcode on a fascia before taking it from a rack.
 */
public final class PickerPickEvent extends EmployeeEvent {

  private final String sku;

  /**
   * Create a new PickerPickEvent.
   *
   * @param name the name of the employee involved in the event
   * @param sku the skanned sku
   *
   * @deprecated Maintained for backwards compatibility purposes, but
   *     {@link PickerPickEvent#PickerPickEvent(String, String)} should be used over this.
   */
  @Deprecated
  public PickerPickEvent(String name, int sku) {
    this(name, Integer.toString(sku));
  }

  /**
   * Create a new PickerPickEvent.
   *
   * @param name the name of the employee involved in the event
   * @param sku the scanned sku
   */
  public PickerPickEvent(String name, String sku) {
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

  @Override
  public String toString() {
    return "Picker " + getName() + " pick " + sku;
  }

  /**
   * Get this event's SKU.
   *
   * @return the sku "scanned" by the picker
   */
  public String getSku() {
    return sku;
  }
}
