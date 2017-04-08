package warehousesimulator.warehouser.injection.modules;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import warehousesimulator.warehouser.EmployeeManager;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class WarehouseModule_EmployeeManagerFactory implements Factory<EmployeeManager> {
  private final WarehouseModule module;

  public WarehouseModule_EmployeeManagerFactory(WarehouseModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public EmployeeManager get() {
    return Preconditions.checkNotNull(
        module.employeeManager(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<EmployeeManager> create(WarehouseModule module) {
    return new WarehouseModule_EmployeeManagerFactory(module);
  }

  /** Proxies {@link WarehouseModule#employeeManager()}. */
  public static EmployeeManager proxyEmployeeManager(WarehouseModule instance) {
    return instance.employeeManager();
  }
}
