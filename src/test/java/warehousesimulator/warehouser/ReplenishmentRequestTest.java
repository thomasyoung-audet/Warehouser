package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class ReplenishmentRequestTest {

  ReplenishmentRequest firstRequest;
  ReplenishmentRequest equalRequest;
  Replenisher replenisher = new Replenisher("Randy");

  @BeforeEach
  void setUp() {
    firstRequest = new ReplenishmentRequest(new WarehouseLocation("A", 0, 1, 1));
    equalRequest = new ReplenishmentRequest(firstRequest.getLocation());
  }

  @Test
  void getLocation() {
    assertEquals(firstRequest.getLocation(), new WarehouseLocation("A", 0, 1, 1));
  }

  @Test
  void getSetAssignee() {
    assertFalse(firstRequest.getAssignee().isPresent());
    firstRequest.setAssignee(replenisher);
    assertTrue(firstRequest.getAssignee().isPresent());
    assertEquals(firstRequest.getAssignee().get(), replenisher);
  }

  @Test
  void replenishmentRequestsAreEqualIfTheyHaveTheSameLocation() {
    assertEquals(firstRequest, equalRequest);
    assertNotEquals(firstRequest, new ReplenishmentRequest(new WarehouseLocation("B", 1, 1, 3)));

    assertNotEquals(firstRequest, new Object());
    assertNotEquals(firstRequest, null);
  }

  @Test
  void equalRequestsHaveIdenticalHashCodes() {
    assertEquals(firstRequest.hashCode(), equalRequest.hashCode());
  }

}