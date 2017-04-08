package warehousesimulator.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import warehousesimulator.warehouser.SkuTranslator;

/**
 * A <Code>SkuTranslator</Code> that bases its translations on a list of strings in csv format.
 */
public class CsvDataSkuTranslator implements SkuTranslator {

  /**
   * A map containing SKUs matched to pairs of colours and models.
   */
  private final Map<String, List<String>> skuMap;


  /**
   * Create a new <code>CsvDataSkuTranslator</code> populating the translation table from a list
   * of Strings.
   *
   * @param translationLines Lines from which to populate the translation table.
   */
  public CsvDataSkuTranslator(List<String> translationLines) {
    skuMap = new HashMap<>();

    for (String translationLine : translationLines) {
      if (translationLine.charAt(0) != '#') {
        String[] translationData = translationLine.split(",");

        List<String> skuList = new ArrayList<>();
        skuList.add(translationData[2]);
        skuList.add(translationData[3]);

        String skuKey = translationData[0] + translationData[1];

        skuMap.put(skuKey, skuList);
      }
    }
  }

  /**
   * Translate the given fascia model and colour into two SKUs.
   *
   * @param model the model of the fascia.
   * @param colour the colour of the fascia.
   * @return A list containing two integer SKUs, where the first integer is the front SKU and the
   *     second is the rear.
   */
  @Override
  public List<String> translate(String model, String colour) {
    return skuMap.getOrDefault(colour + model, new ArrayList<>());
  }
}