package warehousesimulator.warehouser;

import java.util.List;

/**
 * Retrieves the skus for the fascia corresponding to a specific colour and model of car.
 */
public interface SkuTranslator {

  /**
   * Translate a model and colour into front and rear fascia SKUs.
   * 
   * @param model The model of car ordered
   * @param colour The colour of car ordered
   * @return a two element list, the first element is the front sku, the second element is the rear
   *         sku
   */
  List<String> translate(String model, String colour);

}
