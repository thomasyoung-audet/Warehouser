package warehousesimulator.simulation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import warehousesimulator.warehouser.Warehouse;

@RunWith(JUnitPlatform.class)
class ApplicationTest {

  private Application application;
  private static final List<String> stockStrings = Collections.unmodifiableList(
      Collections.singletonList("A,0,0,0,6"));
  @Mock
  private Warehouse warehouse;
  @Mock
  private EventParser parser;
  @Mock
  private SimulatorOutputHandler outputHandler;

  @BeforeEach
  void setApplication() {
    MockitoAnnotations.initMocks(this);
    application = new Application(
        warehouse,
        parser,
        outputHandler,
        stockStrings);
  }


  @Test
  void simulationRunsExactlyOnce() throws IOException {
    Boolean simRunOnce = application.run();
    assertTrue(simRunOnce);

    Boolean simRunTwice = application.run();
    assertFalse(simRunTwice);
    verify(parser, times(1)).sendAllEvents();
  }

  @Test
  void applicationSubscibesWarehouseToEventSource() throws IOException {
    application.run();
    verify(parser).subscribe(warehouse);
  }

  @Test
  void applicationSendsEventsFromParser() throws IOException {
    application.run();
    verify(parser).sendAllEvents();
  }

  @Test
  void applicationSendsWarehouseToWriter() throws IOException {
    application.run();
    verify(outputHandler).write(warehouse);
  }
}