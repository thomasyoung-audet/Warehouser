package warehousesimulator.warehouser.events;

import java.util.Objects;


/**
 * Represents a Loader notifying the warehouse management system that they have scanned a fascia.
 */
public final class LoaderScanEvent extends EmployeeEvent {

  private final String sku;

  /**
   * Create a new LoaderScanEvent.
   *
   * @param name the name of the employee involved in the event
   * @param sku the sku of the scanned fascia
   */
  public LoaderScanEvent(String name, String sku) {
    super(name);
    Objects.requireNonNull(sku);
    this.sku = sku;
  }

  /**
   * Returns the sku being scanned
   *
   * @return the sku.
   */
  public String getSku() {
    return sku;
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
   * Get this <code>LoaderScanEvent</code>'s sku.
   *
   * @return the SKU currently being scanned.
   */
  @Override
  public String toString() {
    return "Loader " + getName() + " scans " + sku;
  }
}
