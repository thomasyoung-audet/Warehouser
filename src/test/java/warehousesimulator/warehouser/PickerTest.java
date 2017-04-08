package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class PickerTest {

  private static final String FIRST_PICKER_NAME = "Peter";

  private final PickingRequest request = new PickingRequest(new ArrayList<>(),
      new ArrayList<>(),
      UUID.randomUUID());

  private Picker firstPicker;

  @BeforeEach
  void setUp() {
    firstPicker = new Picker(FIRST_PICKER_NAME);
  }

  @Test
  void pickersStorePickingRequests() {

    firstPicker.setTask(request);

    assertTrue(firstPicker.getTask().isPresent());
    assertEquals(request, firstPicker.getTask().get());
  }

  @Test
  void pickersWithNullTasksAreReady() {
    assertFalse(firstPicker.getTask().isPresent());
    assertTrue(firstPicker.isReady());
    firstPicker.setTask(request);
    assertFalse(firstPicker.isReady());
  }

  @Test
  void pickersAreEqualIfTheyHaveTheSameName() {
    assertEquals(firstPicker, new Picker(FIRST_PICKER_NAME));
  }

  @Test
  void pickersAreNotEqualToNull() {
    assertNotEquals(firstPicker, null);
  }

  @Test
  void pickersAreNotEqualToOtherEmployeeTypes() {
    assertNotEquals(firstPicker, new Sequencer(FIRST_PICKER_NAME));
    assertNotEquals(firstPicker, new Loader(FIRST_PICKER_NAME));
    assertNotEquals(firstPicker, new Replenisher(FIRST_PICKER_NAME));
  }

}