package warehousesimulator.simulation;

/**
 * Thrown when a string cannot be successfully parsed into an Event.
 */
public class MalformedEventStringException extends Exception {

  /** Default serialization id. */
  private static final long serialVersionUID = 1L;
  private final String eventString;

  public MalformedEventStringException(String eventString) {
    super("Malformed event string: " + eventString);
    this.eventString = eventString;
  }

  /**
   * Get the string responsible for this exception being thrown.
   * @return the unparsable event string.
   */
  public String getEventString() {
    return eventString;
  }
}
