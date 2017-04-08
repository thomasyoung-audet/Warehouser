package warehousesimulator.simulation;

/**
 * Program entry point.
 */
public class WarehouseSimulator {

  /**
   * The main entry point method to the warehouse simulating program.
   *
   * @param args command line arguments passed to the program
   */
  public static void main(String[] args) {
    // Delegate the main method to a non static method for easier testing
    new Launcher().run(args);
  }
}


