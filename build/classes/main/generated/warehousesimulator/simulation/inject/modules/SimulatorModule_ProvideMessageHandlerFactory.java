package warehousesimulator.simulation.inject.modules;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import warehousesimulator.warehouser.communication.MessageHandler;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class SimulatorModule_ProvideMessageHandlerFactory implements Factory<MessageHandler> {
  private final SimulatorModule module;

  public SimulatorModule_ProvideMessageHandlerFactory(SimulatorModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public MessageHandler get() {
    return Preconditions.checkNotNull(
        module.provideMessageHandler(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<MessageHandler> create(SimulatorModule module) {
    return new SimulatorModule_ProvideMessageHandlerFactory(module);
  }

  /** Proxies {@link SimulatorModule#provideMessageHandler()}. */
  public static MessageHandler proxyProvideMessageHandler(SimulatorModule instance) {
    return instance.provideMessageHandler();
  }
}
