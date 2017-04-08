package warehousesimulator.warehouser;

/**
 * An enum representing the status of an {@link Order}.
 */
public enum OrderStatus {

  /**
   * Indicates an {@link Order} has been received by the system.
   */
  RECEIVED,

  /**
   * Indicates an {@link Order} is being picked by a {@link Picker} or awaiting sequencing or
   *   loading.
   */
  Fulfilling,

  /**
   * Indicates an {@link Order} has been loaded onto a truck.
   */
  LOADED
}
