package warehousesimulator.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import warehousesimulator.warehouser.Order;
import warehousesimulator.warehouser.Warehouse;
import warehousesimulator.warehouser.WarehouseLocation;

/**
 * Utility methods for writing to streams.
 */
public class OutputUtils {

  private OutputUtils(){}

  /**
   * Write a list of <code>Order</code>s to a file.
   *
   * @param orders The orders to be written out.
   * @param outputStream The stream to which to write the given orders.
   * @throws IOException if an IO error occurs while writing out the Orders.
   */
  public static void writeOrders(List<Order> orders, OutputStream outputStream) throws IOException {

    List<String> orderStrings = new LinkedList<>();

    for (Order o : orders) {
      orderStrings
          .add(String.format("%1$s,%2$s,%3$d", o.getModel(), o.getColour(), o.getOrderNumber()));
    }

    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      if (orderStrings.isEmpty()) {
        writer.write("");
      }
      for (int i = 0; i < orderStrings.size(); i++) {
        writer.write(orderStrings.get(i));
        if (i < orderStrings.size() - 1) {
          writer.newLine();
        }
      }
    }


  }

  /**
   * Write out comma separated data representing a Warehouses rack spaces with depleted stock
   *
   * @param warehouse The warehouse to get stock data from.
   * @param outputStream The stream to which to write the warehouse stock data
   * @throws IOException If an IO error occurs attempting to write out the stock data
   */
  public static void writeStock(Warehouse warehouse, OutputStream outputStream) throws IOException {
    List<WarehouseLocation> depletedLocations = warehouse.getDiminishedStock();
    List<String> stockStrings = new LinkedList<>();

    Collections.sort(depletedLocations);

    for (WarehouseLocation loc : depletedLocations) {
      stockStrings.add(
          String.format("%1$s,%2$d,%3$d,%4$d,%5$d", loc.getZone(), loc.getAisle(), loc.getRack(),
              loc.getLevel(), warehouse.getStock().get(loc)));
    }

    try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {
      if (depletedLocations.isEmpty()) {
        bufferedWriter.write("");
        return;
      }

      for (int i = 0; i < stockStrings.size(); i++) {
        bufferedWriter.write(stockStrings.get(i));
        if (i < stockStrings.size() - 1) {
          bufferedWriter.newLine();
        }
      }
    }
  }

}
