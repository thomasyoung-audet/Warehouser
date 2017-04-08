package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.*;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import warehousesimulator.warehouser.Warehouse.WarehouseConfig;

@RunWith(JUnitPlatform.class)
class WarehouseConfigTest {


  private static Config config;
  private static WarehouseConfig warehouseConfig;

  @BeforeAll
  static void setupClass() {
    config = ConfigFactory.load("warehouse_config_test").getConfig("warehouse");
    warehouseConfig = new WarehouseConfig(config);
  }

  @Test
  void getZones() {
    assertEquals(config.getStringList("layout.zones"), warehouseConfig.getZones());
  }

  @Test
  void getAisles() {
    assertEquals(config.getInt("layout.aisles"), warehouseConfig.getAisles());
  }

  @Test
  void getRacks() {
    assertEquals(config.getInt("layout.racks"), warehouseConfig.getRacks());
  }

  @Test
  void getLevels() {
    assertEquals(config.getInt("layout.levels"), warehouseConfig.getLevels());
  }

  @Test
  void getDefaultStock() {
    assertEquals(config.getInt("default-stock"), warehouseConfig.getDefaultStock());
  }

}