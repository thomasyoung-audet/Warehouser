package warehousesimulator.warehouser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * An order for a single set of fascia, one front, one rear.
 */
public class Order implements Comparable<Order> {

  /**
   * The colour of the fascia.
   */
  private final String colour;

  /**
   * The model of the fascia.
   */
  private final String model;

  /**
   * The number/id of this order.
   */
  private final int orderNumber;

  /**
   * The SKU of the front fascia.
   */
  private final String frontSku;

  /**
   * The SKU of the rear fascia.
   */
  private final String rearSku;

  /**
   * The status of the order.
   */
  private OrderStatus status = OrderStatus.RECEIVED;

  /**
   * Create a new {@link Order} with the given colour, model, order number, and fascia SKUs.
   *
   * @param colour the colour of the fascia.
   * @param model the model of the fascia.
   * @param orderNumber the number of this order.
   * @param skus a list containing the SKUs for the front and back fascias. The first index is the
   *     front SKU, and the second is the rear.
   */
  public Order(String colour, String model, int orderNumber, List<String> skus) {
    Objects.requireNonNull(colour);
    Objects.requireNonNull(model);
    Objects.requireNonNull(skus);
    if (skus.size() < 2) {
      throw new IllegalArgumentException(
          "skus must have at least two elements but was: " + skus.toString());
    }

    this.frontSku = skus.get(0);
    this.rearSku = skus.get(1);
    this.colour = colour;
    this.model = model;
    this.orderNumber = orderNumber;
  }

  /**
   * Create a new {@link Order} with the given colour, model, order number, and fascia SKUs.
   *
   * @param colour the colour of the fascia.
   * @param model the model of the fascia.
   * @param orderNumber the number of this order.
   * @param frontSku the SKU for the front fascia.
   * @param rearSku the SKU for the rear fascia.
   * @deprecated Prefer {@link #Order(String, String, int, String, String)}
   */
  @Deprecated
  public Order(String colour, String model, int orderNumber, int frontSku, int rearSku) {
    this(colour, model, orderNumber, Integer.toString(frontSku), Integer.toString(rearSku));
  }

  /**
   * Create a new {@link Order} with the given colour, model, order number, and fascia SKUs.
   *
   * @param colour the colour of the fascia.
   * @param model the model of the fascia.
   * @param orderNumber the number of this order.
   * @param frontSku the SKU for the front fascia.
   * @param rearSku the SKU for the rear fascia.
   */
  public Order(String colour, String model, int orderNumber, String frontSku, String rearSku) {
    this(colour, model, orderNumber,
        Arrays.asList(frontSku, rearSku));
  }

  /**
   * Get the colour of the fascia of this order.
   *
   * @return A string representing the colour of the fascia.
   */
  public String getColour() {
    return colour;
  }

  /**
   * Get the model of this order.
   *
   * @return A string representing the model of the fascia.
   */
  public String getModel() {
    return model;
  }

  /**
   * Get the SKU of the rear fascia.
   *
   * @return An integer representing the SKU of the rear fascia.
   */
  public String getRearSku() {
    return rearSku;
  }

  /**
   * Get the SKU of the front fascia.
   *
   * @return An integer representing the SKU of the front fascia.
   */
  public String getFrontSku() {
    return frontSku;
  }

  /**
   * Get the status of this order.
   *
   * @return the status of this order.
   */
  public OrderStatus getStatus() {
    return status;
  }

  /**
   * Set the status of this order.
   *
   * @param status the status to which to set this order's status.
   */
  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  /**
   * Get the number/id of this order.
   *
   * @return the number of this order.
   */
  public int getOrderNumber() {
    return orderNumber;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int compareTo(Order order) {
    return orderNumber - order.orderNumber;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Order order = (Order) obj;

    return new EqualsBuilder()
        .append(orderNumber, order.orderNumber)
        .append(frontSku, order.frontSku)
        .append(rearSku, order.rearSku)
        .append(colour, order.colour)
        .append(model, order.model)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(colour)
        .append(model)
        .append(orderNumber)
        .append(frontSku)
        .append(rearSku)
        .toHashCode();
  }

  @Override
  public String toString() {
    return "Order{"
        + "colour='" + colour + '\''
        + ", model='" + model + '\''
        + ", orderNumber=" + orderNumber
        + ", frontSku='" + frontSku + '\''
        + ", rearSku='" + rearSku + '\''
        + '}';
  }
}
