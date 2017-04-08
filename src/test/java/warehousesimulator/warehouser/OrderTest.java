package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class OrderTest {

  private Order firstOrder;

  @BeforeEach
  void setUp() {
    firstOrder = new Order("Green", "SES", 1, 2, 3);
  }

  @Test
  void validConstructor() {
    assertEquals("Green", firstOrder.getColour());
    assertEquals("SES", firstOrder.getModel());
    assertEquals(1, firstOrder.getOrderNumber());
    assertEquals("2", firstOrder.getFrontSku());
    assertEquals("3", firstOrder.getRearSku());
    assertEquals(OrderStatus.RECEIVED, firstOrder.getStatus());
  }

  @Test
  void nullColour() {
    assertThrows(NullPointerException.class, () ->
        new Order(null, "SES", 1, 2, 3)
    );
  }

  @Test
  void nullModel() {
    assertThrows(NullPointerException.class, () ->
        new Order("Green", null, 1, 5, 23)
    );
  }

  @Test
  void setStatus() {
    Order order = new Order("Blue", "CHE", 3, 8, 19);
    order.setStatus(OrderStatus.Fulfilling);
    assertEquals(OrderStatus.Fulfilling, order.getStatus());
  }

  @Test
  void notEnoughSkus() {
    List<String> skus = Collections.singletonList("ABC1");

    assertThrows(IllegalArgumentException.class, () -> new Order("", "", 0, skus));
  }

  @Test
  void ordersAreComparableByOrderNumber() {
    Order oneOrder = new Order("Blue", "SES", 1, 1, 2);
    Order twoOrder = new Order("Blue", "SES", 2, 1, 2);
    Order threeOrder = new Order("Blue", "SES", 3, 1, 2);

    assertEquals(0, oneOrder.compareTo(oneOrder));
    assertEquals(-1, oneOrder.compareTo(twoOrder));
    assertEquals(-2, oneOrder.compareTo(threeOrder));

    List<Order> orders = Arrays.asList(oneOrder, threeOrder, twoOrder);

    Collections.sort(orders);
    assertEquals(oneOrder, orders.get(0));
    assertEquals(twoOrder, orders.get(1));
    assertEquals(threeOrder, orders.get(2));

    assertNotEquals(oneOrder, null);
  }

  @Test
  void ordersAreEqualIfAllPropertiesAreIdentical() {
    assertEquals(firstOrder,
        new Order(firstOrder.getColour(), firstOrder.getModel(), firstOrder.getOrderNumber(),
            firstOrder.getFrontSku(), firstOrder.getRearSku()));
    assertNotEquals(firstOrder,
        new Order("Wrong", firstOrder.getModel(), firstOrder.getOrderNumber(),
            firstOrder.getFrontSku(), firstOrder.getRearSku()));
    assertNotEquals(firstOrder,
        new Order(firstOrder.getColour(), "Wrong", firstOrder.getOrderNumber(),
            firstOrder.getFrontSku(), firstOrder.getRearSku()));
    assertNotEquals(firstOrder,
        new Order(firstOrder.getColour(), firstOrder.getModel(), 109, firstOrder.getFrontSku(),
            firstOrder.getRearSku()));
    assertNotEquals(firstOrder, new Order(firstOrder.getColour(), firstOrder.getModel(),
        firstOrder.getOrderNumber(), "109", firstOrder.getRearSku()));
    assertNotEquals(firstOrder,
        new Order(firstOrder.getColour(), firstOrder.getModel(), firstOrder.getOrderNumber(),
            firstOrder.getFrontSku(), "109"));
  }

  @Test
  void equalOrdersHaveEqualHashCode() {
    assertEquals(firstOrder.hashCode(), new Order(firstOrder.getColour(), firstOrder.getModel(), firstOrder.getOrderNumber(), firstOrder.getFrontSku(), firstOrder.getRearSku()).hashCode());
  }

}