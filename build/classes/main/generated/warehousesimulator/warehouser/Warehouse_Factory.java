package warehousesimulator.warehouser;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;
import warehousesimulator.warehouser.communication.MessageHandler;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class Warehouse_Factory implements Factory<Warehouse> {
  private final Provider<OrderManager> orderManagerProvider;

  private final Provider<EmployeeManager> employeeManagerProvider;

  private final Provider<MessageHandler> messageHandlerProvider;

  private final Provider<WarehouseNavigator> navigatorProvider;

  private final Provider<Warehouse.WarehouseConfig> warehouseConfigProvider;

  public Warehouse_Factory(
      Provider<OrderManager> orderManagerProvider,
      Provider<EmployeeManager> employeeManagerProvider,
      Provider<MessageHandler> messageHandlerProvider,
      Provider<WarehouseNavigator> navigatorProvider,
      Provider<Warehouse.WarehouseConfig> warehouseConfigProvider) {
    assert orderManagerProvider != null;
    this.orderManagerProvider = orderManagerProvider;
    assert employeeManagerProvider != null;
    this.employeeManagerProvider = employeeManagerProvider;
    assert messageHandlerProvider != null;
    this.messageHandlerProvider = messageHandlerProvider;
    assert navigatorProvider != null;
    this.navigatorProvider = navigatorProvider;
    assert warehouseConfigProvider != null;
    this.warehouseConfigProvider = warehouseConfigProvider;
  }

  @Override
  public Warehouse get() {
    return new Warehouse(
        orderManagerProvider.get(),
        employeeManagerProvider.get(),
        messageHandlerProvider.get(),
        navigatorProvider.get(),
        warehouseConfigProvider.get());
  }

  public static Factory<Warehouse> create(
      Provider<OrderManager> orderManagerProvider,
      Provider<EmployeeManager> employeeManagerProvider,
      Provider<MessageHandler> messageHandlerProvider,
      Provider<WarehouseNavigator> navigatorProvider,
      Provider<Warehouse.WarehouseConfig> warehouseConfigProvider) {
    return new Warehouse_Factory(
        orderManagerProvider,
        employeeManagerProvider,
        messageHandlerProvider,
        navigatorProvider,
        warehouseConfigProvider);
  }
}
