package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class LoaderReadyEventTest {

  private static LoaderReadyEvent event;

  @BeforeAll
  static void classSetUp() {
    event = new LoaderReadyEvent("Larry");
  }

  @Test
  void accept() {
    EventVisitor visitor = mock(EventVisitor.class);
    event.accept(visitor);
    verify(visitor).visit(event);
  }

  @Test
  void testToString() {
    assertEquals("Loader Larry ready", new LoaderReadyEvent("Larry").toString());
  }

}