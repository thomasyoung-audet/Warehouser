package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class PickerReadyEventTest {

  private static PickerReadyEvent event;

  @BeforeAll
  static void setUpClass(){
    event = new PickerReadyEvent("Pamela");
  }

  @Test
  void accept() {

    EventVisitor visitor = mock(EventVisitor.class);
    event.accept(visitor);

    verify(visitor).visit(event);

  }

  @Test
  void toStringTest() {
    assertEquals("Picker Pamela ready", event.toString());
  }

}