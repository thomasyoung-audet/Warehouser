package warehousesimulator.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class MalformedEventStringExceptionTest {

  @Test
  void getEventStringTest() {
    final String nonEvent = "This is so not an event";
    MalformedEventStringException e = new MalformedEventStringException("This is so not an event");
    assertEquals(nonEvent, e.getEventString());

  }

}