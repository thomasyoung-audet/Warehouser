package warehousesimulator.warehouser.events;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class EmployeeEventTest {

  @Test
  void nullNameConstructor() {
    assertThrows(NullPointerException.class, () -> new EmployeeEvent(null) {
      @Override
      public void accept(EventVisitor visitor) {

      }
    });
  }

  @Test
  void getName() {
    EmployeeEvent event = new EmployeeEvent("Name") {
      @Override
      public void accept(EventVisitor visitor) {

      }
    };

    assertEquals("Name", event.getName());
  }

}