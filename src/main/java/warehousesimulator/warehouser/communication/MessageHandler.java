package warehousesimulator.warehouser.communication;

import warehousesimulator.warehouser.Employee;

/**
 * Handles sending messages to Employees.
 */
public interface MessageHandler {

  /**
   * Send a message to an employee.
   *
   * @param employee the Employee to message
   * @param message the contents of the message
   */
  void sendMessage(Employee employee, String message);

}
