package warehousesimulator.simulation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import warehousesimulator.warehouser.Warehouse;
import warehousesimulator.warehouser.WarehouseLocation;

public class Application {

  private static final Pattern STOCK_PATTERN = Pattern
      .compile("^(?<location>[A|B],\\d+,\\d+,\\d+),(?<stock>\\d+)$");

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final Warehouse warehouse;
  private final EventParser parser;
  private final SimulatorOutputHandler outputHandler;
  private final List<String> stockStrings;
  private boolean hasRun = false;

  /**
   * Warehouse simulation entry point.
   */
  @Inject
  public Application(Warehouse warehouse, EventParser parser,
      SimulatorOutputHandler outputHandler,
      List<String> stockStrings) {
    this.warehouse = warehouse;
    this.parser = parser;
    this.outputHandler = outputHandler;
    this.stockStrings = stockStrings;

    this.parser.subscribe(this.warehouse);
  }

  /**
   * Run the simulation. This will only work once per Application object.
   *
   * @return true if the simulation was run.
   */
  public boolean run() throws IOException {
    if (hasRun) {
      return false;
    }

    for (String string : stockStrings) {
      Matcher matcher = STOCK_PATTERN.matcher(string);
      if (matcher.matches()) {
        Optional<WarehouseLocation> location = WarehouseLocation
            .getLocationFromString(matcher.group("location").replace(",", " "));
        if (location.isPresent()) {
          warehouse.getStock().replace(location.get(), Integer.parseInt(matcher.group("stock")));
        } else {
          logger.warn("Cannot set stock with unparsable location string: {}",
              matcher.group("location"));
        }
      }
    }

    parser.sendAllEvents();
    outputHandler.write(warehouse);
    hasRun = true;
    return true;
  }

}
