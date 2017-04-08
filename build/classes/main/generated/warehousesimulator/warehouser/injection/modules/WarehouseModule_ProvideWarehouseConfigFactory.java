package warehousesimulator.warehouser.injection.modules;

import com.typesafe.config.Config;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import warehousesimulator.warehouser.Warehouse;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class WarehouseModule_ProvideWarehouseConfigFactory
    implements Factory<Warehouse.WarehouseConfig> {
  private final WarehouseModule module;

  private final Provider<Config> configProvider;

  public WarehouseModule_ProvideWarehouseConfigFactory(
      WarehouseModule module, Provider<Config> configProvider) {
    assert module != null;
    this.module = module;
    assert configProvider != null;
    this.configProvider = configProvider;
  }

  @Override
  public Warehouse.WarehouseConfig get() {
    return Preconditions.checkNotNull(
        module.provideWarehouseConfig(configProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Warehouse.WarehouseConfig> create(
      WarehouseModule module, Provider<Config> configProvider) {
    return new WarehouseModule_ProvideWarehouseConfigFactory(module, configProvider);
  }

  /** Proxies {@link WarehouseModule#provideWarehouseConfig(Config)}. */
  public static Warehouse.WarehouseConfig proxyProvideWarehouseConfig(
      WarehouseModule instance, Config config) {
    return instance.provideWarehouseConfig(config);
  }
}
