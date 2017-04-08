package warehousesimulator.warehouser.events;

/**
 * Allows an <Code>EventVisitor</Code> to subscribe to Events.
 */
public interface EventSubscribable {

  /**
   * Subscribe to this <code>EventSubscribable</code>
   * @param subscriber the object subscribing for future warehousesimulator.warehouser.events
   */
  void subscribe(EventVisitor subscriber);

}
