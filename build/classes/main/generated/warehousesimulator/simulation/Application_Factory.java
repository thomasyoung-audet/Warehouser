package warehousesimulator.simulation;

import dagger.internal.Factory;
import java.util.List;
import javax.annotation.Generated;
import javax.inject.Provider;
import warehousesimulator.warehouser.Warehouse;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class Application_Factory implements Factory<Application> {
  private final Provider<Warehouse> warehouseProvider;

  private final Provider<EventParser> parserProvider;

  private final Provider<SimulatorOutputHandler> outputHandlerProvider;

  private final Provider<List<String>> stockStringsProvider;

  public Application_Factory(
      Provider<Warehouse> warehouseProvider,
      Provider<EventParser> parserProvider,
      Provider<SimulatorOutputHandler> outputHandlerProvider,
      Provider<List<String>> stockStringsProvider) {
    assert warehouseProvider != null;
    this.warehouseProvider = warehouseProvider;
    assert parserProvider != null;
    this.parserProvider = parserProvider;
    assert outputHandlerProvider != null;
    this.outputHandlerProvider = outputHandlerProvider;
    assert stockStringsProvider != null;
    this.stockStringsProvider = stockStringsProvider;
  }

  @Override
  public Application get() {
    return new Application(
        warehouseProvider.get(),
        parserProvider.get(),
        outputHandlerProvider.get(),
        stockStringsProvider.get());
  }

  public static Factory<Application> create(
      Provider<Warehouse> warehouseProvider,
      Provider<EventParser> parserProvider,
      Provider<SimulatorOutputHandler> outputHandlerProvider,
      Provider<List<String>> stockStringsProvider) {
    return new Application_Factory(
        warehouseProvider, parserProvider, outputHandlerProvider, stockStringsProvider);
  }
}
