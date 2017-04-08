package warehousesimulator.warehouser.communication;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import warehousesimulator.warehouser.Employee;

public class ConsoleMessageHandler implements MessageHandler {

  private final Logger logger = LoggerFactory.getLogger(ConsoleMessageHandler.class);

  @Override
  public void sendMessage(Employee employee, String message) {
    Objects.requireNonNull(employee);
    Objects.requireNonNull(message);
    logger.info("Sending \"{}\" to {}", message, employee.getName());
    System.out.printf("%1$s: %2$s%n", employee.getName(), message);
  }
}
