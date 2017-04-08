package warehousesimulator.warehouser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import warehousesimulator.warehouser.navigation.WarehouseDirection;

/**
 * A picking request consisting of four orders and directions to retreive the fascia necessary to
 * fulfill them.
 */
public class PickingRequest {


  /**
   * The list of orders in this picking request.
   */
  private List<Order> orders;

  /**
   * The list of directions corresponding to each order.
   */
  private List<WarehouseDirection> directions;

  /**
   * The unique ID of this picking request.
   */
  private final UUID uuid;

  /**
   * The picker assigned to complete this picking request.
   */
  private Picker assignedPicker = null;

  /**
   * Create a new {@link PickingRequest} with the given orders, order directions, and id.
   *
   * @param orders the orders of the picking request.
   * @param directions the directions for each SKU in each order.
   * @param uuid the unique ID of this picking request.
   */
  public PickingRequest(List<Order> orders, List<WarehouseDirection> directions, UUID uuid) {
    Objects.requireNonNull(orders);
    Objects.requireNonNull(directions);
    Objects.requireNonNull(uuid);
    this.orders = orders;
    this.directions = directions;
    this.uuid = uuid;
  }

  /**
   * Create a new picking request with the given orders and order directions.
   *
   * @param orders the list of orders in this picking request.
   * @param directions the directions for each SKU in each order.
   */
  public PickingRequest(List<Order> orders, List<WarehouseDirection> directions) {
    this(orders, directions, UUID.randomUUID());
  }

  /**
   * Check if a set of skus match the skus required for ths picking request.
   *
   * @return true if the first four elements of skus match this requests four front fascia the the
   *     last four elements match the four rear fascia.
   */
  public boolean matchSkus(List<String> skus) {
    if (skus.size() != orders.size() * 2) {
      return false;
    }
    return skus.get(0).equals(orders.get(0).getFrontSku())
        && skus.get(1).equals(orders.get(1).getFrontSku())
        && skus.get(2).equals(orders.get(2).getFrontSku())
        && skus.get(3).equals(orders.get(3).getFrontSku())
        && skus.get(4).equals(orders.get(0).getRearSku())
        && skus.get(5).equals(orders.get(1).getRearSku())
        && skus.get(6).equals(orders.get(2).getRearSku())
        && skus.get(7).equals(orders.get(3).getRearSku());
  }


  /**
   * {@inheritDoc}.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    PickingRequest request = (PickingRequest) obj;

    return new EqualsBuilder()
        .append(orders, request.orders)
        .append(directions, request.directions)
        .append(uuid, request.uuid)
        .isEquals();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(orders)
        .append(directions)
        .append(uuid)
        .toHashCode();
  }

  /**
   * Get the {@link Picker} currently assigned to complete this picking request.
   *
   * @return the {@link Picker} currently assigned to this picking request.
   */
  public Optional<Picker> getAssignedPicker() {
    return Optional.ofNullable(assignedPicker);
  }


  /**
   * Assign the given picker to this picking request.
   *
   * @param assignedPicker the {@link Picker} to assign to this picking request.
   */
  public void assign(Picker assignedPicker) {
    this.assignedPicker = assignedPicker;
  }

  /**
   * Get whether or not this picking request has been assigned to a {@link Picker}.
   *
   * @return if this picking request has been assigned or not.
   */
  public boolean isAssigned() {
    return assignedPicker != null;
  }

  /**
   * Get all orders in this picking request.
   *
   * @return the orders of this picking request.
   */
  public List<Order> getOrders() {
    return orders;
  }

  /**
   * Set the status of all Orders included in this PickingRequest.
   *
   * @param status the status to set this PickingRequests's orders to
   */
  public void setAllOrderStatuses(OrderStatus status) {
    Objects.requireNonNull(status);
    for (Order order : orders) {
      order.setStatus(status);
    }
  }

  /**
   * Get the directions for the orders of this picking request.
   *
   * @return A list of this picking request's order directions.
   */
  public List<WarehouseDirection> getDirections() {
    return directions;
  }

  /**
   * Gets this PickingRequest's unique identifier.
   *
   * @return this PickingRequest's UUID
   */
  public UUID getId() {
    return uuid;
  }
}
