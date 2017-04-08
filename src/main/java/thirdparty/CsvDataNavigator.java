package thirdparty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import warehousesimulator.warehouser.WarehouseLocation;
import warehousesimulator.warehouser.navigation.WarehouseDirection;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

/**
 * A {@link WarehouseNavigator} backed by a CSV formatted traversal table. This class provides no
 * true path finding and will simply return directions in the order SKUS appear in its traversal
 * table.
 */
public class CsvDataNavigator implements WarehouseNavigator {

  public static final Pattern TRAVERSAL_DATA_PATTERN = Pattern.compile(
      "(?<location>(?<zone>[A-Z]),(?<aisle>\\d+),"
          + "(?<rack>\\d+),(?<level>\\d+)),(?<sku>[A-Za-z0-9]+)\\s*");
  private static final String GROUP_SKU = "sku";
  private static final String GROUP_ZONE = "zone";
  private static final String GROUP_AISLE = "aisle";
  private static final String GROUP_RACK = "rack";
  private static final String GROUP_LEVEL = "level";
  private static final String GROUP_LOCATION = "location";

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final List<String> traversalData;
  private final List<String> skuIndexes;

  /**
   * Create a new CsvDataNavigator.
   *
   * @param locationData the traversal table for the CsvDataNavigator.
   * @throws InvalidTraversalDataStringException if any line of locationData is not valid traversal
   *     data matching {@link CsvDataNavigator#TRAVERSAL_DATA_PATTERN}
   */
  public CsvDataNavigator(List<String> locationData) throws InvalidTraversalDataStringException {
    Objects.requireNonNull(locationData);
    List<String> traversalDataBuild = new ArrayList<>();
    List<String> skuIndexesBuild = new ArrayList<>();

    for (String data : locationData) {
      if (data.isEmpty() || data.charAt(0) == '#') {
        logger.trace("Skipping empty or commented out line");
        continue;
      }
      Matcher matcher = TRAVERSAL_DATA_PATTERN.matcher(data);

      if (matcher.matches()) {
        logger.trace("Adding traversal data: {}", data);
        traversalDataBuild.add(data);
        skuIndexesBuild.add(matcher.group(GROUP_SKU));
      } else {
        throw new InvalidTraversalDataStringException(data);
      }
    }

    traversalData = traversalDataBuild;
    skuIndexes = skuIndexesBuild;

  }


  /**
   * @param skus Skus to navigate between. Any SKUs that cannot be found in the traversal table will
   *     be dropped.
   */
  @Override
  public List<WarehouseDirection> optimize(List<String> skus) {
    logger.trace("Finding optimal path for skus: " + skus);
    List<WarehouseDirection> optimizedDirections = skus.stream()
        .sorted(Comparator.comparingInt(skuIndexes::indexOf))
        .filter((a) -> {
          if (skuIndexes.contains(a)) {
            return true;
          } else {
            logger.warn("Could not find sku" + TRAVERSAL_DATA_PATTERN.matcher(a).group(GROUP_SKU)
                + "in traversal data");
            return false;
          }
        })
        .map((a) -> {
          Matcher matcher = TRAVERSAL_DATA_PATTERN
              .matcher(traversalData.get(skuIndexes.indexOf(a)));
          matcher.matches();
          WarehouseLocation location = WarehouseLocation
              .getLocationFromString(matcher.group(GROUP_LOCATION).replace(',', ' ')).get();
          return new WarehouseDirection(location, matcher.group(GROUP_SKU));
        })
        .collect(Collectors.toList());
    logger.trace("Optimal path for skus: " + skus + " is " + optimizedDirections);
    return optimizedDirections;
  }

  @Override
  public Optional<WarehouseLocation> locationForSku(String sku) {
    if (skuIndexes.contains(sku)) {
      Matcher matcher = TRAVERSAL_DATA_PATTERN.matcher(traversalData.get(skuIndexes.indexOf(sku)));
      if (matcher.matches()) {
        return WarehouseLocation
            .getLocationFromString(matcher.group(GROUP_LOCATION).replace(',', ' '));
      } else {
        logger.warn("Non matching traversal data {} ", matcher.toString());
      }
    }

    logger.trace("Could not find location for sku: {}", sku);
    return Optional.empty();
  }


  /**
   * Thrown if a line of traversal data cannot be parsed.
   */
  public class InvalidTraversalDataStringException extends Exception {

    /**
     * Create a new InvalidTraversalDataStringException.
     *
     * @param invalidString The string which cannot be read as traversal data
     */
    public InvalidTraversalDataStringException(String invalidString) {
      super(invalidString + " is not a valid navigation string");
    }
  }
}
