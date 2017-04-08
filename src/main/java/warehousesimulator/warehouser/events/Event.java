package warehousesimulator.warehouser.events;

/**
 * Interface for events handled by the warehousing system.
 * @see EventVisitor
 */
public interface Event {

  /**
   * Have a visitor process this event.
   *
   * @param visitor The visitor to receive this event
   */
  void accept(EventVisitor visitor);

}
