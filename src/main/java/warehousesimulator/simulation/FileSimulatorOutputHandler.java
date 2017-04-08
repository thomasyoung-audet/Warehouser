package warehousesimulator.simulation;

import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import warehousesimulator.util.OutputUtils;
import warehousesimulator.warehouser.Warehouse;

public class FileSimulatorOutputHandler implements SimulatorOutputHandler {

  public static final String ORDER_STREAM_TAG =
      "warehousessimulator.simulation.FileSimulatorOutputHandler.orderFileTag";
  public static final String STOCK_STREAM_TAG =
      "warehousessimulator.simulation.FileSimulatorOutputHandler.stockStreamTag";

  private final OutputStream orderStream;
  private final OutputStream stockStream;

  @Inject
  public FileSimulatorOutputHandler(@Named(ORDER_STREAM_TAG) OutputStream orderStream,
      @Named(STOCK_STREAM_TAG) OutputStream stockStream) {
    this.orderStream = orderStream;
    this.stockStream = stockStream;
  }

  @Override
  public void write(Warehouse warehouse) throws IOException {
    OutputUtils.writeOrders(warehouse.getShippedOrders(), orderStream);
    OutputUtils.writeStock(warehouse, stockStream);
  }
}
