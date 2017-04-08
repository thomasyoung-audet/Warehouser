package warehousesimulator.simulation;

import dagger.internal.Factory;
import java.io.OutputStream;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class FileSimulatorOutputHandler_Factory
    implements Factory<FileSimulatorOutputHandler> {
  private final Provider<OutputStream> orderStreamProvider;

  private final Provider<OutputStream> stockStreamProvider;

  public FileSimulatorOutputHandler_Factory(
      Provider<OutputStream> orderStreamProvider, Provider<OutputStream> stockStreamProvider) {
    assert orderStreamProvider != null;
    this.orderStreamProvider = orderStreamProvider;
    assert stockStreamProvider != null;
    this.stockStreamProvider = stockStreamProvider;
  }

  @Override
  public FileSimulatorOutputHandler get() {
    return new FileSimulatorOutputHandler(orderStreamProvider.get(), stockStreamProvider.get());
  }

  public static Factory<FileSimulatorOutputHandler> create(
      Provider<OutputStream> orderStreamProvider, Provider<OutputStream> stockStreamProvider) {
    return new FileSimulatorOutputHandler_Factory(orderStreamProvider, stockStreamProvider);
  }
}
