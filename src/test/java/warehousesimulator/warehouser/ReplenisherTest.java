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
class ReplenisherTest {

  private Replenisher replenisher;

  @BeforeEach
  void setUp() {
    replenisher = new Replenisher("Randy");
  }

  @Test
  void replenishersWithoutTasksAreReady() {
    assertTrue(replenisher.isReady());
  }

  @Test
  void replenishersWithTasksAreNotReady() {
    assertTrue(replenisher.isReady());
    replenisher.setTask(new ReplenishmentRequest(new WarehouseLocation("A", 0, 0, 0)));
    assertFalse(replenisher.isReady());
  }

  @Test
  void getSetTask() {
    ReplenishmentRequest task = new ReplenishmentRequest(new WarehouseLocation("B", 0, 1, 1));
    replenisher.setTask(task);
    assertTrue(replenisher.getTask().isPresent());
    assertTrue(replenisher.getTask().get().equals(task));
  }

  @Test
  void replenishersWithTheSameNameAreEqual() {
    assertEquals(replenisher, new Replenisher(replenisher.getName()));
    assertNotEquals(replenisher, new Replenisher("Wrong"));
  }

  @Test
  void equalReplenishersHaveTheSameHashcode() {
    assertEquals(replenisher.hashCode(), new Replenisher(replenisher.getName()).hashCode());
  }

  @Test
  void replenishersAreNotEqualToOtherObjects() {
    assertNotEquals(replenisher, new Object());
  }

  @Test
  void replenishersAreNotEqualToNull() {
    assertNotEquals(replenisher, null);
  }


}