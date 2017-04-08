package warehousesimulator.warehouser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
class SequencerTest {

  private PickingRequest pickingRequest;

  @BeforeEach
  void setUp() {
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
  void sequencersAreAlwaysReady() {
    Sequencer sequencer = new Sequencer("Sally");
    assertTrue(sequencer.isReady());
  }

  @Test
  void sequencersWithIdenticalNamesAreEqual() {
    assertTrue(new Sequencer("Sally").equals(new Sequencer("Sally")));
  }

  @Test
  void sequencersWithDifferentNamesAreNotEquals() {
    assertFalse(new Sequencer("Alice").equals(new Sequencer("Bob")));
  }

  @Test
  void sequencersAreNotEqualToOtherEmployeeSubtypes() {
    assertFalse(new Sequencer("Sally").equals(new Picker("Sally")));
  }

  @Test
  void equalSequencersHaveTheSameHashcode() {
    assertEquals(new Sequencer("Sally").hashCode(),
        new Sequencer("Sally").hashCode());
  }

  @Test
  void sequencersAreNotEqualToNull() {
    assertFalse(new Sequencer("Sally").equals(null));
  }
}