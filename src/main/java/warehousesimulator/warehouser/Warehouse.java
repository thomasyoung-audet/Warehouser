package warehousesimulator.warehouser;

import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import warehousesimulator.warehouser.communication.MessageHandler;
import warehousesimulator.warehouser.events.EventVisitor;
import warehousesimulator.warehouser.events.LoaderLoadEvent;
import warehousesimulator.warehouser.events.LoaderReadyEvent;
import warehousesimulator.warehouser.events.LoaderRescanEvent;
import warehousesimulator.warehouser.events.LoaderScanEvent;
import warehousesimulator.warehouser.events.OrderEvent;
import warehousesimulator.warehouser.events.PickerMarshalingEvent;
import warehousesimulator.warehouser.events.PickerPickEvent;
import warehousesimulator.warehouser.events.PickerReadyEvent;
import warehousesimulator.warehouser.events.ReplenisherReadyEvent;
import warehousesimulator.warehouser.events.ReplenisherReplenishEvent;
import warehousesimulator.warehouser.events.SequencerReadyEvent;
import warehousesimulator.warehouser.events.SequencerRescanEvent;
import warehousesimulator.warehouser.events.SequencerSequenceEvent;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

public class Warehouse implements EventVisitor {

  private final Logger logger;
  private final WarehouseConfig config;
  private final OrderManager orderManager;
  private final EmployeeManager employeeManager;
  private final MessageHandler messageHandler;
  private final WarehouseNavigator navigator;


  /**
   * The warehouses current stock with each key representing one rack space.
   */
  private final Map<WarehouseLocation, Integer> stock = new HashMap<>();

  /**
   * Picking requests awaiting assignment to a Picker.
   */
  private final Deque<PickingRequest> pickingRequestsAwaitingPicker = new ConcurrentLinkedDeque<>();

  private final Queue<PickingRequest> pickingRequestsAwaitingMarshalling =
      new ConcurrentLinkedQueue<>();

  private final Queue<PickingRequest> pickingRequestsAwaitingLoading =
      new ConcurrentLinkedQueue<>();

  private final Map<Picker, Integer> pickerSteps = new HashMap<>();
  private final Map<Employee, List<String>> scannedSkus = new HashMap<>();

  private final List<ReplenishmentRequest> replenishmentRequests = new ArrayList<>();

  private final List<Order> shippedOrders = new ArrayList<>();


  /**
   * Create a new Warehouse.
   *
   * @param orderManager OrderManager to mange orders in the warehouse
   * @param employeeManager EmployeeManager to manage employees in the warehouse
   * @param messageHandler MessageHandler to send messages to employees in this warehouse
   * @param logger Logger this warehouse
   */
  Warehouse(OrderManager orderManager, EmployeeManager employeeManager,
      MessageHandler messageHandler,
      WarehouseNavigator navigator,
      WarehouseConfig configuration,
      Logger logger) {
    Objects.requireNonNull(messageHandler);
    Objects.requireNonNull(orderManager);
    Objects.requireNonNull(employeeManager);
    Objects.requireNonNull(navigator);
    this.logger = logger;

    logger.info("Creating new Warehouse");
    this.config = configuration;
    this.orderManager = orderManager;
    this.employeeManager = employeeManager;
    this.messageHandler = messageHandler;
    this.navigator = navigator;
    assignPickingRequests();

    for (String zone : config.zones) {
      for (int aisle = 0; aisle < config.aisles; aisle++) {
        for (int rack = 0; rack < config.racks; rack++) {
          for (int level = 0; level < config.levels; level++) {
            stock.put(new WarehouseLocation(zone, aisle, rack, level), config.defaultStock);
          }
        }
      }
    }
  }

  /**
   * Create a new Warehouse.
   *
   * @param orderManager OrderManager to mange orders in the warehouse
   * @param employeeManager EmployeeManager to manage employees in the warehouse
   * @param messageHandler MessageHandler to send messages to employees in this warehouse
   */
  @Inject
  public Warehouse(OrderManager orderManager,
      EmployeeManager employeeManager,
      MessageHandler messageHandler,
      WarehouseNavigator navigator,
      WarehouseConfig warehouseConfig) {
    this(orderManager, employeeManager, messageHandler, navigator, warehouseConfig,
        LoggerFactory.getLogger(Warehouse.class));

  }


  @Override
  public void visit(PickerReadyEvent pickerReadyEvent) {
    logger.info("Received picker ready event: {}", pickerReadyEvent);

    Optional<Picker> pickerOption = employeeManager.getOrCreatePicker(pickerReadyEvent.getName());

    if (pickerOption.isPresent()) {
      Picker picker = pickerOption.get();
      if (picker.getTask().isPresent()) {
        picker.getTask().get().assign(null);
        picker.setTask(null);
      }
      assignPickingRequests();
    } else {
      logger.error("Could not create picker for PickerReadyEvent {}", pickerReadyEvent);
    }
  }

  @Override
  public void visit(PickerPickEvent pickerPickEvent) {
    logger.info("Received picker pick event: {}", pickerPickEvent);
    Optional<Picker> pickerOptional = employeeManager.getPicker(pickerPickEvent.getName());
    if (pickerOptional.isPresent()) {
      Picker picker = pickerOptional.get();
      Optional<PickingRequest> taskOption = picker.getTask();
      Optional<WarehouseLocation> locationOptional = navigator
          .locationForSku(pickerPickEvent.getSku());
      if (taskOption.isPresent()) {
        if (locationOptional.isPresent()) {
          WarehouseLocation location = navigator.locationForSku(pickerPickEvent.getSku()).get();
          PickingRequest task = taskOption.get();

          String sku = pickerPickEvent.getSku();
          if (!pickerSteps.containsKey(picker)) {
            pickerSteps.put(picker, 0);
          }

          int step = pickerSteps.get(picker);

          if (sku.equals(task.getDirections().get(step).getSku())) {
            setRackSpaceStock(location, stock.get(location) - 1);

            step++;

            pickerSteps.replace(picker, step);
            if (step <= 7) {
              messageHandler.sendMessage(picker,
                  String.format("Continue to %1$s for sku %2$s",
                      task.getDirections().get(step).getLocation(),
                      task.getDirections().get(step).getSku())
              );

            } else {
              messageHandler.sendMessage(picker, "Proceed to marshalling");
            }
          } else {
            messageHandler.sendMessage(picker,
                String.format("Incorrect SKU. Continue to %1$s for %2$s",
                    task.getDirections().get(step).getLocation(),
                    task.getDirections().get(step).getSku()
                )
            );
          }
        } else {
          logger.error("Pick event for sku without location: {})", pickerPickEvent.getSku());
        }
      } else {
        logger.error("Picker {} picking without assigned task", picker);
      }
    } else {
      logger.error("Picker {} in event {} does not exist", pickerPickEvent.getName(),
          pickerPickEvent.toString());
    }

  }

  @Override
  public void visit(PickerMarshalingEvent pickerMarshalingEvent) {
    logger.info("Received picker marshaling event: {}", pickerMarshalingEvent);
    Optional<Picker> pickerOption = employeeManager
        .getPicker(pickerMarshalingEvent.getName());
    if (pickerOption.isPresent()) {
      Picker picker = pickerOption.get();
      pickerSteps.remove(picker);
      Optional<PickingRequest> task = picker.getTask();
      if (task.isPresent()) {
        pickingRequestsAwaitingMarshalling.add(task.get());
        task.get().assign(null);
      } else {
        logger.error("Picker {} at marshalling without assigned task.", picker.getName());
      }
    } else {
      logger.error("Picker {} in event {} does not exist", pickerMarshalingEvent.getName(),
          pickerMarshalingEvent.toString());
    }


  }

  @Override
  public void visit(SequencerReadyEvent sequencerReadyEvent) {
    logger.info("Received sequencer ready event: {}", sequencerReadyEvent);
    employeeManager.getOrCreateSequencer(sequencerReadyEvent.getName());


  }


  @Override
  public void visit(SequencerSequenceEvent sequencerSequenceEvent) {
    logger.info("Received sequencer sequence event: {}", sequencerSequenceEvent);

    Optional<Sequencer> sequencerOptional = employeeManager
        .getSequencer(sequencerSequenceEvent.getName());
    if (!sequencerOptional.isPresent()) {
      logger.warn("Irrational Event: {} with non-existent sequencer", sequencerSequenceEvent);
      return;
    }

    if (pickingRequestsAwaitingMarshalling.isEmpty()) {
      logger.warn("Irrational Event: {} without request awaiting marshalling",
          sequencerSequenceEvent);
      return;
    }

    final Sequencer sequencer = sequencerOptional.get();
    final String sku = sequencerSequenceEvent.getSku();
    final PickingRequest request = pickingRequestsAwaitingMarshalling.peek();

    scannedSkus.putIfAbsent(sequencer, new ArrayList<>());
    List<String> localScannedSkus = scannedSkus.get(sequencer);
    localScannedSkus.add(sku);
    messageHandler.sendMessage(sequencer, "Scanned: " + sku);

    if (localScannedSkus.size() == request.getDirections().size()) {
      if (request.matchSkus(localScannedSkus)) {
        messageHandler.sendMessage(sequencer, "Skus match. Send for loading.");
        pickingRequestsAwaitingLoading.add(pickingRequestsAwaitingMarshalling.poll());
      } else {
        messageHandler.sendMessage(sequencer, "Sku mismatch. Discard.");
        addPickingRequestToHeadOfPickingQueue(pickingRequestsAwaitingMarshalling.poll());
      }
      scannedSkus.remove(sequencer);
    }


  }

  @Override
  public void visit(SequencerRescanEvent sequencerRescanEvent) {
    logger.info("Received sequencer rescan event: {}", sequencerRescanEvent);

    Optional<Sequencer> sequencerOptional = employeeManager
        .getOrCreateSequencer(sequencerRescanEvent.getName());

    if (sequencerOptional.isPresent()) {
      Sequencer sequencer = sequencerOptional.get();
      messageHandler.sendMessage(sequencer, "Scan Cleared");
      scannedSkus.remove(sequencer);
    } else {
      logger.warn("Irrational Event: ({}) with non-existent sequencer",
          sequencerRescanEvent.getName());
    }
  }

  @Override
  public void visit(LoaderReadyEvent loaderReadyEvent) {
    logger.info("Received loader ready event: {}", loaderReadyEvent);
    employeeManager.getOrCreateLoader(loaderReadyEvent.getName());

  }

  @Override
  public void visit(LoaderRescanEvent loaderRescanEvent) {
    logger.info("Received loader rescan event: {}", loaderRescanEvent);
    Optional<Loader> loaderOptional = employeeManager
        .getLoader(loaderRescanEvent.getName());

    loaderOptional.ifPresent(scannedSkus::remove);
  }

  @Override
  public void visit(LoaderScanEvent loaderScanEvent) {
    logger.info("Received loader scan event: {}", loaderScanEvent);

    Optional<Loader> loaderOptional = employeeManager
        .getLoader(loaderScanEvent.getName());

    if (!loaderOptional.isPresent()) {
      logger.warn("Irrational event: ({}) with non-existent loader", loaderScanEvent);
      return;
    }

    if (pickingRequestsAwaitingLoading.isEmpty()) {
      logger.warn("Irrational event: ({}) without request awaiting loading", loaderScanEvent);
      return;
    }

    final PickingRequest request = pickingRequestsAwaitingLoading.peek();
    Loader loader = loaderOptional.get();
    String sku = loaderScanEvent.getSku();
    scannedSkus.putIfAbsent(loader, new ArrayList<>());
    List<String> localScannedSkus = this.scannedSkus.get(loader);
    localScannedSkus.add(sku);

    messageHandler.sendMessage(loader, "Scanned: " + sku);

    if (localScannedSkus.size() == request.getDirections().size()) {
      if (request.matchSkus(localScannedSkus)) {
        messageHandler.sendMessage(loader, "Skus Valid. Load.");
      } else {
        messageHandler.sendMessage(loader, "Sku Mismatch. Discard");
        addPickingRequestToHeadOfPickingQueue(request);
      }
      scannedSkus.remove(loader);
    }


  }

  @Override
  public void visit(LoaderLoadEvent loaderLoadEvent) {
    logger.info("Received loader load event: {}", loaderLoadEvent);

    Optional<Loader> loaderOptional = employeeManager.getLoader(loaderLoadEvent.getName());

    if (pickingRequestsAwaitingLoading.isEmpty()) {
      logger.warn("Irrational Event: {}, without request ready to load");
      return;
    }

    if (!loaderOptional.isPresent()) {
      logger.warn("Irrational Event: {}, with non-existent loader", loaderLoadEvent);
      return;
    }

    PickingRequest request = pickingRequestsAwaitingLoading.poll();

    request.setAllOrderStatuses(OrderStatus.LOADED);
    for (Order order : request.getOrders()) {
      shippedOrders.add(order);
      logger.trace("Order {} loaded by {}", order, loaderLoadEvent.getName());
    }
  }

  @Override
  public void visit(OrderEvent orderEvent) {
    logger.info("Received order event: {}", orderEvent);
    orderManager.newOrder(orderEvent.getModel(), orderEvent.getColour());

    packageOrders();

  }


  @Override
  public void visit(ReplenisherReplenishEvent replenisherReplenishEvent) {
    logger.info("Received replenisher replenish event: {}", replenisherReplenishEvent);
    Optional<WarehouseLocation> location = WarehouseLocation
        .getLocationFromString(replenisherReplenishEvent.getLocation());

    if (location.isPresent()) {
      setRackSpaceStock(location.get(), stock.get(location.get()) + 25);

      Optional<Replenisher> replenisherOptional = employeeManager
          .getOrCreateReplenisher(replenisherReplenishEvent.getName());

      if (replenisherOptional.isPresent()) {
        Replenisher replenisher = replenisherOptional.get();

        if (!replenisher.getTask().isPresent()) {
          logger.error("Replenisher {} replenishing with null task", replenisher.getName());
          return;
        } else {
          ReplenishmentRequest request = replenisher.getTask().get();
          if (!replenishmentRequests.contains(request)
              || !request.getLocation().equals(location.get())) {
            logger.error("Replenisher {} fulfilling unassigned task. Refilling {}. Assigned {}",
                replenisher.getName(),
                location,
                request.getLocation());
            logger.error(replenishmentRequests.toString());
          }

          replenishmentRequests.remove(request);
        }
      } else {

        logger.error("Replenisher {} in event {} does not exist",
            replenisherReplenishEvent.getName(),
            replenisherReplenishEvent);
      }
    } else {
      logger.error("ReplenisherReplenishEvent with unparsable location: {}",
          replenisherReplenishEvent.getLocation());
    }

  }

  @Override
  public void visit(ReplenisherReadyEvent replenisherReadyEvent) {
    logger.info("Received replenisher ready event: {}", replenisherReadyEvent);

    Optional<Replenisher> replenisherOptional = employeeManager
        .getOrCreateReplenisher(replenisherReadyEvent.getName());
    if (replenisherOptional.isPresent()) {
      replenisherOptional.get().setTask(null);
      assignReplenishmentRequests();
    } else {
      logger.error("Replenisher {} in event {} could not be created",
          replenisherReadyEvent.getName(),
          replenisherReadyEvent);
    }

  }

  public Map<WarehouseLocation, Integer> getStock() {
    return stock;
  }

  /**
   * Get all locations in this warehouse with stock below this warehouses default stock value.
   *
   * @return A list of <Code>WarehouseLocation</Code>s with stock below this Warehouse's default
   *     stock value.
   */
  public List<WarehouseLocation> getDiminishedStock() {
    List<WarehouseLocation> diminishedLocations = new LinkedList<>();
    for (WarehouseLocation location : stock.keySet()) {
      if (stock.get(location) < config.getDefaultStock()) {
        diminishedLocations.add(location);
      }
    }

    return diminishedLocations;
  }

  public List<Order> getShippedOrders() {
    return shippedOrders;
  }

  /**
   * Set the stock at a give location to a new value.
   *
   * @param location The location to set stock at
   * @param newStock The new value of that stock. If this is above this warehouses default stock
   *     value it will be set to that default value. If below 0, stock will be set to 0.
   */
  private void setRackSpaceStock(WarehouseLocation location, int newStock) {
    if (newStock > config.getDefaultStock()) {
      logger.warn("Attempting to set stock at {} to {}. Maximum stock is {}.", location, newStock,
          config.getDefaultStock());
      newStock = config.defaultStock;
    }
    if (newStock < 0) {
      logger.warn("Attempting to set stock at {} to a negative value of {}.", location, newStock);
      newStock = 0;
    }
    if (stock.containsKey(location)) {
      logger.trace("{} current stock: {}", location, stock.get(location));
      stock.replace(location, newStock);
      logger.trace("{} new stock: {}", location, stock.get(location));
      if (stock.get(location) <= 5) {
        registerReplenishmentRequest(location);
      }
    } else {
      logger.warn("Attempt to modify non-existent warehouse location: {}", location);
    }
  }

  private void registerReplenishmentRequest(WarehouseLocation location) {
    ReplenishmentRequest request = new ReplenishmentRequest(location);
    if (!replenishmentRequests.contains(request)) {
      logger.info("Registering replenishment request for {}", location);
      replenishmentRequests.add(request);
    }

    assignReplenishmentRequests();
  }

  private void assignReplenishmentRequests() {
    List<Replenisher> readyReplenishers = employeeManager.getReplenishers().stream()
        .filter(Replenisher::isReady)
        .collect(Collectors.toList());
    List<ReplenishmentRequest> openRequests = replenishmentRequests.stream()
        .filter((r) -> !r.getAssignee().isPresent())
        .collect(Collectors.toList());

    while (!readyReplenishers.isEmpty() && !openRequests.isEmpty()) {
      Replenisher replenisher = readyReplenishers.remove(0);
      ReplenishmentRequest request = openRequests.remove(0);
      replenisher.setTask(request);
      request.setAssignee(replenisher);
      messageHandler.sendMessage(replenisher,
          String.format("Replenish rack space %1$s", request.getLocation()));
    }
  }

  /**
   * Attempt to assign any picking requests currently waiting for pickers.
   */
  private void assignPickingRequests() {
    logger.debug("Trying to assign picking requests");
    List<Picker> readyPickers = employeeManager.getReadyPickers();

    while (!readyPickers.isEmpty() && !pickingRequestsAwaitingPicker.isEmpty()) {
      PickingRequest request = pickingRequestsAwaitingPicker.poll();
      Picker picker = readyPickers.remove(0);
      request.assign(picker);
      picker.setTask(request);

      messageHandler.sendMessage(picker, String.format("New assignment %s", request.getId()));
      messageHandler.sendMessage(picker, String
          .format("Proceed to %1$s for %2$s", request.getDirections().get(0).getLocation(),
              request.getDirections().get(0).getSku()));
    }
  }

  private void packageOrders() {
    logger.info("Attempting to package orders");
    List<Order> availableOrders = orderManager.getOrdersByStatus(OrderStatus.RECEIVED);
    while (availableOrders.size() >= 4) {
      logger.trace("Four or more orders waiting, creating picking request");
      List<Order> packagingOrders = availableOrders.subList(0, 4);
      List<String> skus = new ArrayList<>();
      packagingOrders.forEach((order -> {
        skus.add(order.getFrontSku());
        skus.add(order.getRearSku());
        order.setStatus(OrderStatus.Fulfilling);
      }));

      PickingRequest request = new PickingRequest(packagingOrders,
          navigator.optimize(skus));
      logger.info("New picking request: {} with Directions: {} and Orders: {}", request.getId(),
          request.getDirections(), request.getOrders());
      pickingRequestsAwaitingPicker.add(request);
      availableOrders = orderManager.getOrdersByStatus(OrderStatus.RECEIVED);
    }

    assignPickingRequests();
  }

  private void addPickingRequestForPicking(PickingRequest request) {
    logger.info("Picking Request {} awaiting picker", request);
    request.setAllOrderStatuses(OrderStatus.Fulfilling);
    pickingRequestsAwaitingPicker.add(request);
    assignPickingRequests();
  }

  private void addPickingRequestToHeadOfPickingQueue(PickingRequest request) {
    logger.info("Picking Request {} added to head of picking queue");
    request.setAllOrderStatuses(OrderStatus.Fulfilling);
    pickingRequestsAwaitingPicker.addFirst(request);
    assignPickingRequests();
  }

  /**
   * Configuration class for {@link Warehouse Warehouses}.
   */
  public static class WarehouseConfig {

    private final List<String> zones;
    private final int aisles;
    private final int racks;
    private final int levels;
    private final int defaultStock;

    /**
     * Construct a new WarehouseConfig.
     *
     * @param config Warehouse configuration data. Must contain the following:
     * <pre>
     * 'layout.zones' List of zones within the warehouse. 'layout.aisles' Number of aisles per
     * zone.
     * 'layout.racks' Number of racks per aisle. 'layout.levels' Number of levels on each rack.
     * 'default-stock' Starting stock level rack spaces.
     * </pre>
     */
    public WarehouseConfig(Config config) {
      zones = config.getStringList("layout.zones");
      aisles = config.getInt("layout.aisles");
      racks = config.getInt("layout.racks");
      levels = config.getInt("layout.levels");
      defaultStock = config.getInt("default-stock");
    }

    /**
     * Get zones.
     *
     * @return a list of strings representing the zones that the configured warehouse should contain
     */
    public List<String> getZones() {
      return new ArrayList<>(zones);
    }

    /**
     * Get aisle count.
     *
     * @return the number of aisles contained within each zones in the configured warehouse.
     */
    public int getAisles() {
      return aisles;
    }

    /**
     * Get rack count.
     *
     * @return the number of racks in each aisle in the configured warehouse.
     */
    public int getRacks() {
      return racks;
    }

    /**
     * Get level count.
     *
     * @return The number of levels on each rack in the configured warehouse.
     */
    public int getLevels() {
      return levels;
    }

    /**
     * Get default stock level.
     *
     * @return the default stock level for rack spaces in the configured warehouse.
     */
    public int getDefaultStock() {
      return defaultStock;
    }
  }
}

