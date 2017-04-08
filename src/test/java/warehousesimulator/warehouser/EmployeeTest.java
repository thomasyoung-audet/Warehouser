package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
public class EmployeeTest {

  @Test
  public void getName() {
    Employee employee = new Employee("Carl") {
      @Override
      public boolean isReady() {
        return false;
      }
    };

    assertEquals("Carl", employee.getName());
  }

  @Test
  void nullNameConstructor() {
    assertThrows(NullPointerException.class, () -> new Employee(null) {

      @Override
      public boolean isReady() {
        return false;
      }
    });
  }


}