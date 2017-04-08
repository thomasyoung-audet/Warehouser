package warehousesimulator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import warehousesimulator.warehouser.Order;
import warehousesimulator.warehouser.Warehouse;
import warehousesimulator.warehouser.WarehouseLocation;

@RunWith(JUnitPlatform.class)
class OutputUtilsTest {

  @Mock
  private Warehouse warehouse;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void writeOrders() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    List<Order> orders = Arrays.asList(
        new Order("Green", "SES", 1, 1, 1),
        new Order("White", "S", 2, 1, 1),
        new Order("Tan", "SE", 3, 1, 1)
    );
    OutputUtils.writeOrders(orders, outputStream);

    assertEquals("SES,Green,1" + System.lineSeparator()
            + "S,White,2" + System.lineSeparator()
            + "SE,Tan,3",
        outputStream.toString());
  }

  @Test
  void writeEmptyOrders() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputUtils.writeOrders(new ArrayList<>(), outputStream);
    assertEquals("", outputStream.toString());
  }

  @Test
  void writeStock() throws IOException {
    List<WarehouseLocation> locations = Arrays.asList(
        new WarehouseLocation("A", 0, 0, 1),
        new WarehouseLocation("B", 0, 2, 1),
        new WarehouseLocation("A", 1, 0, 3)
    );
    Map<WarehouseLocation, Integer> stock = new HashMap<>();
    for (WarehouseLocation l : locations) {
      stock.put(l, 25);
    }

    when(warehouse.getDiminishedStock()).thenReturn(locations);
    when(warehouse.getStock()).thenReturn(stock);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    OutputUtils.writeStock(warehouse, outputStream);

    assertEquals("A,0,0,1,25" + System.lineSeparator()
            + "A,1,0,3,25" + System.lineSeparator()
            + "B,0,2,1,25"
        , outputStream.toString());
  }

  @Test
  void writeUndiminishedStock() throws IOException {
    when(warehouse.getDiminishedStock()).thenReturn(new ArrayList<>());
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputUtils.writeStock(warehouse, outputStream);
    assertEquals("", outputStream.toString());
  }

  @Test
  void instantiateOutputUtils() throws NoSuchMethodException, InstantiationException, InvocationTargetException, IllegalAccessException {
    Constructor<OutputUtils> constructor = OutputUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    assertNotNull(constructor.newInstance());
  }
}
