package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class OrderEventTest {

  private static OrderEvent event;

  @BeforeAll
  static void classSetUP() {
    event = new OrderEvent("SES", "Blue");
  }

  @Test
  void accept() {
    EventVisitor visitor = mock(EventVisitor.class);
    OrderEvent event = new OrderEvent("ABC", "Green");
    event.accept(visitor);
    verify(visitor).visit(event);
  }

  @Test
  void getModel() {
    assertEquals("SES", event.getModel());
  }

  @Test
  void getColour() {
    assertEquals("Blue", event.getColour());
  }

  @Test
  void nullColourConstructor() {
    assertThrows(NullPointerException.class, () ->
        new OrderEvent("SomeModel", null));
  }

  @Test
  void nullModelConstructor() {
    assertThrows(NullPointerException.class, () ->
        new OrderEvent(null, "SomeColour"));
  }

  @Test
  void testToString() {
    assertEquals("Order SES Blue", event.toString());
  }

}