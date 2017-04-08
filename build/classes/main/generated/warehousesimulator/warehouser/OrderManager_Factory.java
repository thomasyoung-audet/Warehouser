package warehousesimulator.warehouser;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class OrderManager_Factory implements Factory<OrderManager> {
  private final Provider<SkuTranslator> translatorProvider;

  public OrderManager_Factory(Provider<SkuTranslator> translatorProvider) {
    assert translatorProvider != null;
    this.translatorProvider = translatorProvider;
  }

  @Override
  public OrderManager get() {
    return new OrderManager(translatorProvider.get());
  }

  public static Factory<OrderManager> create(Provider<SkuTranslator> translatorProvider) {
    return new OrderManager_Factory(translatorProvider);
  }
}
