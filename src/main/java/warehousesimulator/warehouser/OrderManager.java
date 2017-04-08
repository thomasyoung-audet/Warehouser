package warehousesimulator.warehouser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages {@link Order orders} within a single {@link Warehouse warehouse}.
 */
public class OrderManager {

  /**
   * The list of all orders ever created.
   */
  private List<Order> orders;

  /**
   * Keeps track of the current order number. Used when creating new orders.
   */
  private int ordernumber = 1;

  /**
   * Used for creating new orders.
   */
  private final SkuTranslator translator;

  private final Logger logger = LoggerFactory.getLogger(OrderManager.class);

  /**
   * Create a new <code>OrderManager</code> with the given {@link SkuTranslator}.
   *
   * @param translator the {@link SkuTranslator} to use when creating new orders.
   */
  @Inject
  public OrderManager(SkuTranslator translator) {
    this(translator, new ArrayList<>());
  }

  /**
   * Create a new <code>OrderManager</code> with the given {@link SkuTranslator} and existing
   * orders.
   *
   * @param translator the {@link SkuTranslator} to use when creating new orders.
   * @param orders the list of existing orders.
   */
  public OrderManager(SkuTranslator translator, List<Order> orders) {
    Objects.requireNonNull(translator);
    Objects.requireNonNull(orders);

    logger.info("Creating new OrderManager with {} and initial orders: {}",
        translator.getClass().getSimpleName(), orders);

    this.orders = orders;
    this.translator = translator;
  }

  /**
   * Creates and stores a new Order ensuring that orders have unique sequential order numbers.
   *
   * @param model the model of car ordered
   * @param colour the colour of car ordered
   * @return the newly created Order.
   */
  public Order newOrder(String model, String colour) {
    Order newOrder = new Order(colour, model, ordernumber, translator.translate(model, colour));
    orders.add(newOrder);
    ordernumber++;

    logger.debug("Creating new order with model: {} colour: {}, ordernumber{}, skus {}&{}",
        newOrder.getModel(),
        newOrder.getColour(),
        newOrder.getOrderNumber(), newOrder.getFrontSku(), newOrder.getRearSku());
    return newOrder;

  }

  /**
   * Get all orders managed by this {@link OrderManager}.
   *
   * @return all orders this manager currently manages.
   */
  public List<Order> getOrders() {
    return orders;
  }

  /**
   * Get an {@link Order} by it's {@link Order#getOrderNumber()}.
   *
   * @param id the order number of the {@link Order}.
   * @return the order represented by the given id, or <code>null</code> if the order does not exist
   *     in this manager.
   */
  public Optional<Order> getOrderById(int id) {
    Optional<Order> optional = Optional.empty();

    for (Order order : orders) {
      if (order.getOrderNumber() == id) {
        optional = Optional.of(order);
      }
    }

    return optional;
  }

  /**
   * Return a list of all orders with the given {@link OrderStatus}.
   *
   * @param status The status to get orders with.
   * @return A list of all orders with the given status.
   */
  public List<Order> getOrdersByStatus(OrderStatus status) {
    return orders.stream().filter(order -> order.getStatus() == status)
        .collect(Collectors.toList());
  }

}
