package warehousesimulator.warehouser.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import warehousesimulator.warehouser.Employee;

@RunWith(JUnitPlatform.class)
class ConsoleMessageHandlerTest {

  private static PrintStream stdout;
  private static String TEST_MESSAGE = "TEST_MESSAGE";

  @Mock
  private Employee employee;
  private ByteArrayOutputStream output;
  private MessageHandler messageHandler;

  @BeforeAll
  static void classSetup() {
    stdout = System.out;
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    messageHandler = new ConsoleMessageHandler();
  }

  @AfterAll
  static void classTeardown() {
    System.setOut(stdout);
  }


  @Test
  void sendMessage() {
    when(employee.getName()).thenReturn("Name");
    messageHandler.sendMessage(employee, TEST_MESSAGE);

    assertEquals(employee.getName() + ": " + TEST_MESSAGE + System.lineSeparator(), output.toString());
  }

  @Test
  void nullEmployeeThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () ->
        messageHandler.sendMessage(null, TEST_MESSAGE));
  }

  @Test
  void nullMessageThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () ->
        messageHandler.sendMessage(employee, null)
    );
  }


}