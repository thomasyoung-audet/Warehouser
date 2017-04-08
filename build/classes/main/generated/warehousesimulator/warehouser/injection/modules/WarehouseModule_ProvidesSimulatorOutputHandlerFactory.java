package warehousesimulator.warehouser.injection.modules;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import warehousesimulator.simulation.FileSimulatorOutputHandler;
import warehousesimulator.simulation.SimulatorOutputHandler;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class WarehouseModule_ProvidesSimulatorOutputHandlerFactory
    implements Factory<SimulatorOutputHandler> {
  private final WarehouseModule module;

  private final Provider<FileSimulatorOutputHandler> handlerProvider;

  public WarehouseModule_ProvidesSimulatorOutputHandlerFactory(
      WarehouseModule module, Provider<FileSimulatorOutputHandler> handlerProvider) {
    assert module != null;
    this.module = module;
    assert handlerProvider != null;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public SimulatorOutputHandler get() {
    return Preconditions.checkNotNull(
        module.providesSimulatorOutputHandler(handlerProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<SimulatorOutputHandler> create(
      WarehouseModule module, Provider<FileSimulatorOutputHandler> handlerProvider) {
    return new WarehouseModule_ProvidesSimulatorOutputHandlerFactory(module, handlerProvider);
  }

  /** Proxies {@link WarehouseModule#providesSimulatorOutputHandler(FileSimulatorOutputHandler)}. */
  public static SimulatorOutputHandler proxyProvidesSimulatorOutputHandler(
      WarehouseModule instance, FileSimulatorOutputHandler handler) {
    return instance.providesSimulatorOutputHandler(handler);
  }
}
