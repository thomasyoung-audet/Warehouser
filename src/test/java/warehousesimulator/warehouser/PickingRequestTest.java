package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import warehousesimulator.warehouser.navigation.WarehouseDirection;

@RunWith(JUnitPlatform.class)
class PickingRequestTest {

  private static final List<Order> ORDERS = Arrays.asList(
      new Order("Blue", "SES", 1, 1, 2),
      new Order("Blue", "SES", 2, 3, 4),
      new Order("Blue", "SES", 3, 5, 6),
      new Order("Blue", "SES", 4, 7, 8)
  );
  private static final List<Order> LESSORDERS = Arrays.asList(
          new Order("Blue", "SES", 1, 1, 1)
  );
  private static final List<WarehouseDirection> LOCATIONS = Arrays.asList(
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 1),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 2),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 3),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 4),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 5),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 6),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 7),
      new WarehouseDirection(new WarehouseLocation("A", 1, 1, 1), 8)
  );

  private static PickingRequest firstRequest;

  @BeforeEach
  void setUp() {
    firstRequest = new PickingRequest(ORDERS, LOCATIONS);
  }

  @Test
  void validConstructor() {

    PickingRequest request = new PickingRequest(ORDERS, LOCATIONS);

    assertEquals(ORDERS, request.getOrders());
    assertEquals(LOCATIONS, request.getDirections());
    assertFalse(request.getAssignedPicker().isPresent());
    assertFalse(request.isAssigned());
  }

  @Test
  void isAssignedIfAssignedPickerNotNull() {
    PickingRequest request = new PickingRequest(new ArrayList<>(), new ArrayList<>());
    assertFalse(request.isAssigned());
    request.assign(new Picker("Carl"));
    assertTrue(request.isAssigned());
  }

  @Test
  void setAssignedPicker() {
    PickingRequest request = new PickingRequest(new ArrayList<>(), new ArrayList<>());
    Picker picker = new Picker("Carl");
    request.assign(picker);
    assertTrue(request.getAssignedPicker().isPresent());
    assertEquals(picker, request.getAssignedPicker().get());
  }

  @Test
  void pickingRequestsAreEqualIfAllPropertiesAreEqual() {
    assertEquals(firstRequest, new PickingRequest(ORDERS, LOCATIONS, firstRequest.getId()));

    assertNotEquals(firstRequest,
        new PickingRequest(new ArrayList<>(), LOCATIONS, firstRequest.getId()));
    assertNotEquals(firstRequest, new PickingRequest(ORDERS, new ArrayList<>(), firstRequest.getId()));
    assertNotEquals(firstRequest, new PickingRequest(ORDERS, LOCATIONS, UUID.randomUUID()));
  }

  @Test
  void pickingRequestsAreNotEqualToOtherTypes() {
    assertNotEquals(firstRequest, new Object());
  }

  @Test
  void pickingRequestsAreNotEqualToNull() {
    assertNotEquals(firstRequest, null);
  }

  @Test
  void equalPickingRequestsHaveIdenticalHashCodes() {
    assertEquals(firstRequest.hashCode(), new PickingRequest(ORDERS, LOCATIONS, firstRequest.getId()).hashCode());
  }
  @Test
  void setAllOrderStatusesTest() {
    PickingRequest request = new PickingRequest(LESSORDERS, LOCATIONS);
    request.setAllOrderStatuses(OrderStatus.LOADED);
    assertEquals(OrderStatus.LOADED, LESSORDERS.get(0).getStatus());
  }

  @Test
  void matchShortSkuList() {
    assertFalse(firstRequest.matchSkus(Arrays.asList("1", "2")));
  }

  @Test
  void matchLongSkuList() {
    assertFalse(firstRequest.matchSkus(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9")));
  }

  @Test
  void matchMatchingSkus() {
    assertTrue(firstRequest.matchSkus(Arrays.asList("1", "3", "5", "7", "2", "4", "6", "8")));
  }

  @Test void misMatchSkus() {
    assertFalse(firstRequest.matchSkus(Arrays.asList("123", "12", "5", "7", "2", "4", "6", "8")));
  }
}