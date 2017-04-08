package warehousesimulator.warehouser.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import warehousesimulator.warehouser.WarehouseLocation;

@RunWith(JUnitPlatform.class)
class WarehouseDirectionTest {

  private static WarehouseDirection warehouseDirection;

  @BeforeAll
  static void setUp() {
    warehouseDirection = new WarehouseDirection(
        new WarehouseLocation("A", 0, 0, 0), "1");
  }


  @Test
  void getLocation() {
    assertEquals(new WarehouseLocation("A", 0, 0, 0),
        warehouseDirection.getLocation());
  }

  @Test
  void getSku() {
    assertEquals("1", warehouseDirection.getSku());
  }

  @Test
  void warehouseDirectionsAreEqualIfTheyHaveTheSameLocationAndSku() {
    WarehouseDirection copyDirection = new WarehouseDirection(warehouseDirection.getLocation(),
        warehouseDirection.getSku());
    assertEquals(warehouseDirection, copyDirection);
  }

  @Test
  void warehouseDirectionsAreNotEqualWithDifferentLocations() {
    WarehouseDirection nonEqualDirection = new WarehouseDirection(
        new WarehouseLocation("A", 1, 1, 1), warehouseDirection.getSku());
    assertNotEquals(warehouseDirection, nonEqualDirection);
  }

  @Test
  void warehouseLocationsAreNotEqualWithDifferentSkus() {
    WarehouseDirection nonEqualDirection = new WarehouseDirection(warehouseDirection.getLocation(),
        "758");
  }

  @Test
  void locationToString() {
    assertEquals("A 0 0 0 for sku 1", warehouseDirection.toString());
  }

  @Test
  void objEqualsTest() {
    assertTrue(warehouseDirection.equals(warehouseDirection));
  }

  @Test
  void nullObjEqualTest() {
    assertFalse(warehouseDirection.equals(null));
  }
}