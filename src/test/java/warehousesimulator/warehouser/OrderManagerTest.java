package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitPlatform.class)
class OrderManagerTest {

  private OrderManager orderManager;
  @Mock private SkuTranslator translator;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    orderManager = new OrderManager(translator);
    when(translator.translate(anyString(), anyString())).thenReturn(Arrays.asList("1", "2"));
  }


  @Test
  void newOrderCreatesOrdersWithSequentialIds() {
    Order firstOrder = orderManager.newOrder("Foo", "Bar");
    assertEquals(1, firstOrder.getOrderNumber());
    int lastOrderNumber = firstOrder.getOrderNumber();

    for (int i = 0; i < 100; i++) {
      Order nextOrder = orderManager.newOrder("Foo", "Bar");
      assertEquals(lastOrderNumber + 1, nextOrder.getOrderNumber());
      lastOrderNumber = nextOrder.getOrderNumber();
    }
  }

  @Test
  void getOrderByOrderNumber() {
    Order firstOrder = orderManager.newOrder("Foo", "Bar");

    for (int i = 0; i < 100; i++) {
      orderManager.newOrder("Foo", "Bar");
    }

    Optional<Order> orderById = orderManager.getOrderById(1);
    assertTrue(orderById.isPresent());
    assertEquals(firstOrder, orderById.get());
  }

  @Test
  void getNonexistentOrderByNumber() {
    assertFalse(orderManager.getOrderById(5).isPresent());
  }


  @Test
  void getOrdersByStatus() {
    Order firstOrder = orderManager.newOrder("ABC", "Magenta");
    Order secondOrder = orderManager.newOrder("DEF", "Fuchsia");
    secondOrder.setStatus(OrderStatus.Fulfilling);
    Order thirdOrder = orderManager.newOrder("LMN", "Brown");
    thirdOrder.setStatus(OrderStatus.LOADED);

    List<Order> receivedOrders = orderManager.getOrdersByStatus(OrderStatus.RECEIVED);
    List<Order> pickingOrders = orderManager.getOrdersByStatus(OrderStatus.Fulfilling);
    List<Order> loadedOrders = orderManager.getOrdersByStatus(OrderStatus.LOADED);

    assertTrue(receivedOrders.contains(firstOrder));
    assertFalse(receivedOrders.contains(secondOrder));
    assertFalse(receivedOrders.contains(secondOrder));

    assertFalse(pickingOrders.contains(firstOrder));
    assertTrue(pickingOrders.contains(secondOrder));
    assertFalse(pickingOrders.contains(thirdOrder));

    assertFalse(loadedOrders.contains(firstOrder));
    assertFalse(loadedOrders.contains(secondOrder));
    assertTrue(loadedOrders.contains(thirdOrder));
  }

  @Test
  void getOrders() {
    List<Order> orders = Arrays.asList(
        new Order("Black", "Mod", 1, 2, 3),
        new Order("Black", "Mod", 2, 2, 3),
        new Order("Black", "Mod", 3, 2, 3),
        new Order("Black", "Mod", 4, 2, 3),
        new Order("Black", "Mod", 5, 2, 3)
    );
    OrderManager prefilledOrderManager = new OrderManager(translator, orders);
    assertTrue(prefilledOrderManager.getOrders().containsAll(orders));
  }

  @Test
  void nullTranslatorShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () ->
        new OrderManager(null)
    );
  }

  @Test
  void nullOrderListShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () ->
        new OrderManager(translator, null)
    );
  }


}