package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RunWith(JUnitPlatform.class)
class LoaderTest {
  
  private final static String FIRST_LOADER_NAME = "Larry";

  private Loader firstLoader;
  private PickingRequest pickingRequest;

  @BeforeEach
  void setUp() {
    firstLoader = new Loader(FIRST_LOADER_NAME);

    List<String> skusOrder1 = new ArrayList<>();
    skusOrder1.add("skuFront1");
    skusOrder1.add("skuRear1");

    List<String> skusOrder2 = new ArrayList<>();
    skusOrder2.add("skuFront2");
    skusOrder2.add("skuRear2");

    List<String> skusOrder3 = new ArrayList<>();
    skusOrder3.add("skuFront3");
    skusOrder3.add("skuRear3");

    List<String> skusOrder4 = new ArrayList<>();
    skusOrder4.add("skuFront4");
    skusOrder4.add("skuRear4");

    List<Order> orders = new ArrayList<>();
    orders.add(new Order("color1", "model1", 1, skusOrder1));
    orders.add(new Order("color2", "model2", 2, skusOrder2));
    orders.add(new Order("color3", "model3", 3, skusOrder3));
    orders.add(new Order("color4", "model4", 4, skusOrder4));

    pickingRequest = new PickingRequest(orders, new ArrayList<>());
  }

  @Test
  void loadersAlwaysReady() {
    assertTrue(new Loader(FIRST_LOADER_NAME).isReady());
  }

  @Test
  void loadersAreEqualIfTheyHaveTheSameName() {
    assertEquals(firstLoader, new Loader(FIRST_LOADER_NAME));
  }
  
  @Test
  void loadersAreNotEqualToNull() {
    assertNotEquals(firstLoader, null);
  }
  
  @Test
  void loadersAreNotEqualToOtherEmployeeClasses() {
    assertNotEquals(firstLoader, new Picker(FIRST_LOADER_NAME));
    assertNotEquals(firstLoader, new Sequencer(FIRST_LOADER_NAME));
    assertNotEquals(firstLoader, new Replenisher(FIRST_LOADER_NAME));
  }

  @Test
  void equalLoadersHaveIdenticalHashCodes() {
    assertEquals(firstLoader.hashCode(), new Loader(FIRST_LOADER_NAME).hashCode());
  }

}