package warehousesimulator.simulation;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.CsvDataNavigator;
import thirdparty.CsvDataNavigator.InvalidTraversalDataStringException;
import warehousesimulator.simulation.inject.components.AppComponent;
import warehousesimulator.simulation.inject.components.DaggerAppComponent;
import warehousesimulator.util.InputUtils;

/**
 * Main entry point for the warehouse simulation.
 * {@link Launcher#run(String[])} is used in place of {@link WarehouseSimulator#main(String[])}
 * for testing purposes.
 *
 */
class Launcher {

  private final Logger logger = LoggerFactory.getLogger(Launcher.class.getName());

  /**
   * Run the warehouse simulation.
   *
   * @param args Commandline arguments passed to the program.
   */
  /** Run the simulation.
   *
   * @param args Command line arguments
   * @return true if the simulation was run.
   */
  boolean run(String[] args) {
    if (args == null) {
      args = new String[]{};
    }
    File eventsFile;
    if (args.length > 0) {
      eventsFile = new File(args[0]);
    } else {
      eventsFile = new File(System.getProperty("user.dir"), "events.txt");
    }
    Config config = ConfigFactory.load();

    logger.info("Entering launcher.run");

    try {

      // Load events and create the EventSubscribable (a simple event parser for now)
      if (!eventsFile.exists()) {
        Files.copy(getClass().getResourceAsStream("events/default.txt"), eventsFile.toPath());
      }
      if (!eventsFile.canRead()) {
        logger.error("Cannot read events file: {}", eventsFile.getPath());
        return false;
      }
      Path eventsPath = eventsFile.toPath();
      logger.debug("Running with events file: {}", eventsPath);

      // Load initial stock data and set warehouse stock.
      File stockFile = new File(System.getProperty("user.dir"), "initial.csv");
      if (!stockFile.exists()) {
        Files.copy(getClass().getResourceAsStream("stock/default.csv"), stockFile.toPath());
      }
      Path stockPath = stockFile.toPath();

      logger.debug("Running with stock file: {}", stockPath);

      // Create a warehouse using the default translation data
      AppComponent appComponent = DaggerAppComponent.builder()
          .warehouseConfig(config.getConfig("warehouse"))
          .eventParser(new EventParser(Files.readAllLines(eventsPath)))
          .skuTranslator(new CsvDataSkuTranslator(InputUtils
              .readLinesFromStream(getClass().getResourceAsStream("translation/default.csv"))))
          .navigator(new CsvDataNavigator(InputUtils.readLinesFromStream(
              CsvDataNavigator.class.getResourceAsStream("traversal_table_string_skus.csv"))))
          .stockStrings(Files.readAllLines(stockPath))
          .ordersOutputStream(new FileOutputStream(
              new File(System.getProperty("user.dir"), config.getString("simulation.order-file"))))
          .stockOutputStream(new FileOutputStream(new File(System.getProperty("user.dir"),
              config.getString("simulation.final-stock-file"))))
          .build();

      Application application = appComponent.app();
      return application.run();


    } catch (IOException exception) {
      logger.error(exception.toString());
      exception.printStackTrace();
    } catch (MalformedEventStringException exception) {
      logger.error(exception.toString());
      System.err.printf("Could not parse event: %1$s%n", exception.getEventString());
    } catch (InvalidTraversalDataStringException e) {
      logger.error(e.toString());
    }

    return false;
  }

}
