package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class WarehouseLocationTest {

  @Test
  void validConstructor() {
    WarehouseLocation location = new WarehouseLocation("A", 1, 0, 2);
    assertEquals("A", location.getZone());
    assertEquals(1, location.getAisle());
    assertEquals(0, location.getRack());
    assertEquals(2, location.getLevel());
  }

  @Test
  void nullZoneConstructor() {
    assertThrows(NullPointerException.class, () ->
        new WarehouseLocation(null, 0, 0, 0));
  }

  @Test
  void toStringTest() {
    WarehouseLocation location = new WarehouseLocation("A", 1, 2, 3);
    assertEquals("A 1 2 3", location.toString());
  }

  @Test
  void getLocationFromString() {
    Optional<WarehouseLocation> location = WarehouseLocation.getLocationFromString("A 1 1 1");
    assertTrue(location.isPresent());
    assertEquals("A", location.get().getZone());
    assertEquals(1, location.get().getAisle());
    assertEquals(1, location.get().getRack());
    assertEquals(1, location.get().getLevel());

    location = WarehouseLocation.getLocationFromString("B 2 1 0");
    assertTrue(location.isPresent());
    assertEquals("B", location.get().getZone());
    assertEquals(2, location.get().getAisle());
    assertEquals(1, location.get().getRack());
    assertEquals(0, location.get().getLevel());
  }

  @Test
  void nonMatchingString() {
    Optional<WarehouseLocation> location = WarehouseLocation
        .getLocationFromString("Not a valid location");
    assertFalse(location.isPresent());
  }

  @Test
  void objEqualsTest(){
    WarehouseLocation location = new WarehouseLocation("A", 1, 2, 3);
    assertTrue(location.equals(location));
  }
  @Test
  void nullObjEqualTest(){
    WarehouseLocation location = new WarehouseLocation("A", 1, 2, 3);
    assertFalse(location.equals(null));
  }
  @Test
  void compareToOtherRackTest() {
    WarehouseLocation location = new WarehouseLocation("A", 1, 2, 3);
    WarehouseLocation location2 = new WarehouseLocation("A", 1, 3, 3);
    assertEquals(location2.compareTo(location), 1);
  }
  @Test
  void compareToOtherLevelTest() {
    WarehouseLocation location = new WarehouseLocation("A", 1, 2, 2);
    WarehouseLocation location2 = new WarehouseLocation("A", 1, 2, 3);
    assertEquals(location2.compareTo(location), 1);
  }
}