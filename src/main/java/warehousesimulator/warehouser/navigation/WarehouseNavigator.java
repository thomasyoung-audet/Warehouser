package warehousesimulator.warehouser.navigation;

import java.util.List;
import java.util.Optional;
import warehousesimulator.warehouser.WarehouseLocation;

/**
 * General interface for classes tha provide navigation information for a single {@link
 * warehousesimulator.warehouser.Warehouse Warehouse}
 */
public interface WarehouseNavigator {

  /**
   * Find the optimal path between a list of SKUs.
   *
   * @param skus Skus to navigate between
   * @return A list of WarehouseDirections in order o the optimal path between them
   */
  List<WarehouseDirection> optimize(List<String> skus);

  /**
   * Get the location in the warehouse for a particular SKU.
   *
   * @param sku The SKU for which to get the warehouse location.
   * @return the location of the SKU in the warehouse, null if the sku is not found.
   */
  Optional<WarehouseLocation> locationForSku(String sku);

}
