package warehousesimulator.simulation;

import java.io.IOException;
import warehousesimulator.warehouser.Warehouse;

public interface SimulatorOutputHandler {
  void write(Warehouse warehouse) throws IOException;
}
