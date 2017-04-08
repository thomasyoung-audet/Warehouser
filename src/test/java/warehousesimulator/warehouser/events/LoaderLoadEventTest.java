package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class LoaderLoadEventTest {

  private static LoaderLoadEvent event;

  @BeforeAll
  static void classSetUp(){
    event = new LoaderLoadEvent("Larry");
  }

  @Test
  void accept() {
    EventVisitor visitor = mock(EventVisitor.class);
    LoaderLoadEvent e = new LoaderLoadEvent("Name");
    e.accept(visitor);
    verify(visitor).visit(e);

  }

  @Test
  void testToString() {
    assertEquals("Loader Larry loads", event.toString());
  }


}