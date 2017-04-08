package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class SequencerReadyEventTest {

  private static SequencerReadyEvent event;

  @BeforeAll
  static void setupClass(){
    event = new SequencerReadyEvent("Sally");
  }

  @Test
  void accept() {
    EventVisitor visitor = mock(EventVisitor.class);
    event.accept(visitor);

    verify(visitor).visit(event);
  }

  @Test
  void testToString() {
    assertEquals("Sequencer Sally ready", event.toString());
  }

}