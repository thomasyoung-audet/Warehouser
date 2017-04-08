package thirdparty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import thirdparty.CsvDataNavigator.InvalidTraversalDataStringException;
import warehousesimulator.warehouser.WarehouseLocation;
import warehousesimulator.warehouser.navigation.WarehouseDirection;

@RunWith(JUnitPlatform.class)
class CsvDataNavigatorTest {

  private static final List<String> SKUS = Arrays.asList(
      "SW2",
      "SW1",
      "SELW1",
      "SELW2",
      "SEW1",
      "SEW2",
      "SESW2",
      "SESW1"
  );
  private static CsvDataNavigator navigator;

  @BeforeAll
  static void createNavigator() throws InvalidTraversalDataStringException {
    navigator = new CsvDataNavigator(
        Arrays.asList(
            "A,0,0,0,SW1",
            "A,0,0,1,SW2",
            "A,0,0,2,SEW1",
            "A,0,0,3,SEW2",
            "A,0,1,0,SESW1",
            "A,0,1,1,SESW2",
            "A,0,1,2,SELW1",
            "A,0,1,3,SELW2",
            "A,0,2,0,SB1",
            "A,0,2,1,SB2"
        )
    );
  }

  @Test
  void optimize() {
    List<WarehouseDirection> path = navigator.optimize(SKUS);
    assertEquals("SW1", path.get(0).getSku());
    assertEquals("SW2", path.get(1).getSku());
    assertEquals("SEW1", path.get(2).getSku());
    assertEquals("SEW2", path.get(3).getSku());
    assertEquals("SESW1", path.get(4).getSku());
    assertEquals("SESW2", path.get(5).getSku());
    assertEquals("SELW1", path.get(6).getSku());
    assertEquals("SELW2", path.get(7).getSku());
  }

  @Test
  void getlocationForSku() {
    Optional<WarehouseLocation> location = navigator.locationForSku("SB2");
    assertEquals(location.get(), new WarehouseLocation("A", 0, 2, 1));
  }

  @Test
  void getNonExistentSkuLocation() {
    assertFalse(navigator.locationForSku("Not a valid sku").isPresent());
  }

  @Test
  void invalidTraversalDataThrowsException() {
    assertThrows(CsvDataNavigator.InvalidTraversalDataStringException.class,
        () -> new CsvDataNavigator(
            Collections.singletonList("Not valid traversal data")));
  }

}