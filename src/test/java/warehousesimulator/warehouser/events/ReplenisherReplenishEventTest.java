package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class ReplenisherReplenishEventTest {

  private static ReplenisherReplenishEvent event;

  @BeforeAll
  static void setupClass() {
    event = new ReplenisherReplenishEvent("Rand", "A 0 1 0");
  }

  @Test
  void accept() {
    EventVisitor visitor = mock(EventVisitor.class);
    event.accept(visitor);

    verify(visitor).visit(event);
  }

  @Test
  void getLocation() {
    assertEquals("A 0 1 0", event.getLocation());
  }

  @Test
  void testToString() {
    assertEquals("Replenisher Rand replenish A 0 1 0", event.toString());
  }

}