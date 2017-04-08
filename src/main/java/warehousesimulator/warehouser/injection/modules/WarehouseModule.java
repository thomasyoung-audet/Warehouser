package warehousesimulator.warehouser.injection.modules;

import com.typesafe.config.Config;
import dagger.Module;
import dagger.Provides;
import warehousesimulator.simulation.FileSimulatorOutputHandler;
import warehousesimulator.simulation.SimulatorOutputHandler;
import warehousesimulator.warehouser.EmployeeManager;
import warehousesimulator.warehouser.Warehouse.WarehouseConfig;
import warehousesimulator.warehouser.injection.annotation.WarehouseConfigData;

@Module
public class WarehouseModule {

  @Provides
  WarehouseConfig provideWarehouseConfig(@WarehouseConfigData Config config) {
    return new WarehouseConfig(config);
  }

  @Provides
  EmployeeManager employeeManager() {
    return new EmployeeManager();
  }

  @Provides
  SimulatorOutputHandler providesSimulatorOutputHandler(FileSimulatorOutputHandler handler) {
    return handler;
  }

}
