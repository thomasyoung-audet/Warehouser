package warehousesimulator.simulation.inject.components;

import com.typesafe.config.Config;
import dagger.internal.InstanceFactory;
import dagger.internal.Preconditions;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.Generated;
import javax.inject.Provider;
import warehousesimulator.simulation.Application;
import warehousesimulator.simulation.Application_Factory;
import warehousesimulator.simulation.EventParser;
import warehousesimulator.simulation.FileSimulatorOutputHandler;
import warehousesimulator.simulation.FileSimulatorOutputHandler_Factory;
import warehousesimulator.simulation.SimulatorOutputHandler;
import warehousesimulator.simulation.inject.modules.SimulatorModule;
import warehousesimulator.simulation.inject.modules.SimulatorModule_ProvideMessageHandlerFactory;
import warehousesimulator.warehouser.EmployeeManager;
import warehousesimulator.warehouser.OrderManager;
import warehousesimulator.warehouser.OrderManager_Factory;
import warehousesimulator.warehouser.SkuTranslator;
import warehousesimulator.warehouser.Warehouse;
import warehousesimulator.warehouser.Warehouse_Factory;
import warehousesimulator.warehouser.communication.MessageHandler;
import warehousesimulator.warehouser.injection.modules.WarehouseModule;
import warehousesimulator.warehouser.injection.modules.WarehouseModule_EmployeeManagerFactory;
import warehousesimulator.warehouser.injection.modules.WarehouseModule_ProvideWarehouseConfigFactory;
import warehousesimulator.warehouser.injection.modules.WarehouseModule_ProvidesSimulatorOutputHandlerFactory;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerAppComponent implements AppComponent {
  private Provider<SkuTranslator> skuTranslatorProvider;

  private Provider<OrderManager> orderManagerProvider;

  private Provider<EmployeeManager> employeeManagerProvider;

  private Provider<MessageHandler> provideMessageHandlerProvider;

  private Provider<WarehouseNavigator> navigatorProvider;

  private Provider<Config> warehouseConfigProvider;

  private Provider<Warehouse.WarehouseConfig> provideWarehouseConfigProvider;

  private Provider<Warehouse> warehouseProvider;

  private Provider<EventParser> eventParserProvider;

  private Provider<OutputStream> ordersOutputStreamProvider;

  private Provider<OutputStream> stockOutputStreamProvider;

  private Provider<FileSimulatorOutputHandler> fileSimulatorOutputHandlerProvider;

  private Provider<SimulatorOutputHandler> providesSimulatorOutputHandlerProvider;

  private Provider<List<String>> stockStringsProvider;

  private Provider<Application> applicationProvider;

  private DaggerAppComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static AppComponent.Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.skuTranslatorProvider = InstanceFactory.create(builder.skuTranslator);

    this.orderManagerProvider = OrderManager_Factory.create(skuTranslatorProvider);

    this.employeeManagerProvider =
        WarehouseModule_EmployeeManagerFactory.create(builder.warehouseModule);

    this.provideMessageHandlerProvider =
        SimulatorModule_ProvideMessageHandlerFactory.create(builder.simulatorModule);

    this.navigatorProvider = InstanceFactory.create(builder.navigator);

    this.warehouseConfigProvider = InstanceFactory.create(builder.warehouseConfig);

    this.provideWarehouseConfigProvider =
        WarehouseModule_ProvideWarehouseConfigFactory.create(
            builder.warehouseModule, warehouseConfigProvider);

    this.warehouseProvider =
        Warehouse_Factory.create(
            orderManagerProvider,
            employeeManagerProvider,
            provideMessageHandlerProvider,
            navigatorProvider,
            provideWarehouseConfigProvider);

    this.eventParserProvider = InstanceFactory.create(builder.eventParser);

    this.ordersOutputStreamProvider = InstanceFactory.create(builder.ordersOutputStream);

    this.stockOutputStreamProvider = InstanceFactory.create(builder.stockOutputStream);

    this.fileSimulatorOutputHandlerProvider =
        FileSimulatorOutputHandler_Factory.create(
            ordersOutputStreamProvider, stockOutputStreamProvider);

    this.providesSimulatorOutputHandlerProvider =
        WarehouseModule_ProvidesSimulatorOutputHandlerFactory.create(
            builder.warehouseModule, fileSimulatorOutputHandlerProvider);

    this.stockStringsProvider = InstanceFactory.create(builder.stockStrings);

    this.applicationProvider =
        Application_Factory.create(
            warehouseProvider,
            eventParserProvider,
            providesSimulatorOutputHandlerProvider,
            stockStringsProvider);
  }

  @Override
  public Application app() {
    return new Application(
        new Warehouse(
            new OrderManager(skuTranslatorProvider.get()),
            employeeManagerProvider.get(),
            provideMessageHandlerProvider.get(),
            navigatorProvider.get(),
            provideWarehouseConfigProvider.get()),
        eventParserProvider.get(),
        providesSimulatorOutputHandlerProvider.get(),
        stockStringsProvider.get());
  }

  private static final class Builder implements AppComponent.Builder {
    private WarehouseModule warehouseModule;

    private SimulatorModule simulatorModule;

    private SkuTranslator skuTranslator;

    private Config warehouseConfig;

    private WarehouseNavigator navigator;

    private EventParser eventParser;

    private OutputStream stockOutputStream;

    private OutputStream ordersOutputStream;

    private List<String> stockStrings;

    @Override
    public AppComponent build() {
      if (warehouseModule == null) {
        this.warehouseModule = new WarehouseModule();
      }
      if (simulatorModule == null) {
        this.simulatorModule = new SimulatorModule();
      }
      if (skuTranslator == null) {
        throw new IllegalStateException(SkuTranslator.class.getCanonicalName() + " must be set");
      }
      if (warehouseConfig == null) {
        throw new IllegalStateException(Config.class.getCanonicalName() + " must be set");
      }
      if (navigator == null) {
        throw new IllegalStateException(
            WarehouseNavigator.class.getCanonicalName() + " must be set");
      }
      if (eventParser == null) {
        throw new IllegalStateException(EventParser.class.getCanonicalName() + " must be set");
      }
      if (stockOutputStream == null) {
        throw new IllegalStateException(OutputStream.class.getCanonicalName() + " must be set");
      }
      if (ordersOutputStream == null) {
        throw new IllegalStateException(OutputStream.class.getCanonicalName() + " must be set");
      }
      if (stockStrings == null) {
        throw new IllegalStateException(List.class.getCanonicalName() + " must be set");
      }
      return new DaggerAppComponent(this);
    }

    @Override
    public Builder warehouseModule(WarehouseModule module) {
      this.warehouseModule = Preconditions.checkNotNull(module);
      return this;
    }

    @Override
    public Builder simulatorModule(SimulatorModule module2) {
      this.simulatorModule = Preconditions.checkNotNull(module2);
      return this;
    }

    @Override
    public Builder skuTranslator(SkuTranslator translator) {
      this.skuTranslator = Preconditions.checkNotNull(translator);
      return this;
    }

    @Override
    public Builder warehouseConfig(Config config) {
      this.warehouseConfig = Preconditions.checkNotNull(config);
      return this;
    }

    @Override
    public Builder navigator(WarehouseNavigator navigator) {
      this.navigator = Preconditions.checkNotNull(navigator);
      return this;
    }

    @Override
    public Builder eventParser(EventParser parser) {
      this.eventParser = Preconditions.checkNotNull(parser);
      return this;
    }

    @Override
    public Builder stockOutputStream(OutputStream stream) {
      this.stockOutputStream = Preconditions.checkNotNull(stream);
      return this;
    }

    @Override
    public Builder ordersOutputStream(OutputStream stream2) {
      this.ordersOutputStream = Preconditions.checkNotNull(stream2);
      return this;
    }

    @Override
    public Builder stockStrings(List<String> stockStrings) {
      this.stockStrings = Preconditions.checkNotNull(stockStrings);
      return this;
    }
  }
}
