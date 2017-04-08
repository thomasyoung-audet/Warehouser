package warehousesimulator.warehouser.events;

import java.util.Objects;

/**
 * Represents a new order being entered into the system.
 */
public final class OrderEvent implements Event {

  private final String model;
  private final String colour;

  /**
   * Create a new <code>OrderEvent</code>.
   *
   * @param model the model of car being ordered
   * @param colour the colour of car being ordered
   */
  public OrderEvent(String model, String colour) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(colour);
    this.model = model;
    this.colour = colour;
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
    return "Order " + model + " " + colour;
  }

  /**
   * Get this event's colour.
   *
   * @return the colour of car ordered in this event
   */
  public String getColour() {
    return colour;
  }

  /**
   * Get this warehousesimulator.warehouser.events model.
   *
   * @return the model of car ordered in this event
   */
  public String getModel() {
    return model;
  }
}
