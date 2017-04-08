package warehousesimulator.simulation.inject.components;

import com.typesafe.config.Config;
import dagger.BindsInstance;
import dagger.Component;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Named;
import warehousesimulator.simulation.Application;
import warehousesimulator.simulation.EventParser;
import warehousesimulator.simulation.FileSimulatorOutputHandler;
import warehousesimulator.simulation.inject.modules.SimulatorModule;
import warehousesimulator.warehouser.SkuTranslator;
import warehousesimulator.warehouser.injection.annotation.WarehouseConfigData;
import warehousesimulator.warehouser.injection.modules.WarehouseModule;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

@Component(modules = {WarehouseModule.class, SimulatorModule.class})
public interface AppComponent {

  Application app();

  @Component.Builder
  interface Builder {

    Builder warehouseModule(WarehouseModule module);

    Builder simulatorModule(SimulatorModule module);

    @BindsInstance
    Builder skuTranslator(SkuTranslator translator);

    @BindsInstance
    Builder warehouseConfig(@WarehouseConfigData Config config);

    @BindsInstance
    Builder navigator(WarehouseNavigator navigator);

    @BindsInstance
    Builder eventParser(EventParser parser);

    @BindsInstance
    Builder stockOutputStream(
        @Named(FileSimulatorOutputHandler.STOCK_STREAM_TAG) OutputStream stream);

    @BindsInstance
    Builder ordersOutputStream(
        @Named(FileSimulatorOutputHandler.ORDER_STREAM_TAG) OutputStream stream);

    @BindsInstance
    Builder stockStrings(List<String> stockStrings);

    AppComponent build();
  }

}
