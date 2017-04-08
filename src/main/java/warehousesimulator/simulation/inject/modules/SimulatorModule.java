package warehousesimulator.simulation.inject.modules;

import dagger.Module;
import dagger.Provides;
import warehousesimulator.warehouser.communication.ConsoleMessageHandler;
import warehousesimulator.warehouser.communication.MessageHandler;

@Module
public class SimulatorModule {

  @Provides
  MessageHandler provideMessageHandler() {
    return new ConsoleMessageHandler();
  }



}
