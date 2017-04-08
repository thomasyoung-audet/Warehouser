package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.typesafe.config.ConfigFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import warehousesimulator.warehouser.Warehouse.WarehouseConfig;
import warehousesimulator.warehouser.communication.MessageHandler;
import warehousesimulator.warehouser.events.PickerMarshalingEvent;
import warehousesimulator.warehouser.events.PickerPickEvent;
import warehousesimulator.warehouser.events.PickerReadyEvent;
import warehousesimulator.warehouser.events.SequencerReadyEvent;
import warehousesimulator.warehouser.navigation.WarehouseDirection;
import warehousesimulator.warehouser.navigation.WarehouseNavigator;

@RunWith(JUnitPlatform.class)
class WarehouseTest {

  private static final WarehouseConfig config = new WarehouseConfig(
      ConfigFactory.load("warehouse_config_test").getConfig("warehouse"));
  @Mock
  private OrderManager orderManager;
  @Mock
  private EmployeeManager employeeManager;
  @Mock
  private MessageHandler messageHandler;
  @Mock
  private WarehouseNavigator navigator;
  @Mock
  private Logger logger;


  private Warehouse warehouse;
  private Picker defaultPicker;
  private PickingRequest request;

  private Sequencer defaultSequencer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    warehouse = new Warehouse(orderManager, employeeManager, messageHandler, navigator, config,
        logger);
    defaultPicker = new Picker("Picker");
    request = Mockito.spy(new PickingRequest(
        Arrays.asList(
            new Order("Colour", "Model", 1, "Front1", "Rear1"),
            new Order("Colour", "Model", 1, "Front2", "Rear2"),
            new Order("Colour", "Model", 1, "Front3", "Rear3"),
            new Order("Colour", "Model", 1, "Front4", "Rear4")
        ),
        Arrays.asList(
            new WarehouseDirection(new WarehouseLocation("A", 0, 0, 0), "Front1"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 0, 1), "Rear1"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 0, 2), "Front2"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 0, 3), "Rear2"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 1, 0), "Front3"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 1, 1), "Rear3"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 1, 2), "Front4"),
            new WarehouseDirection(new WarehouseLocation("A", 0, 1, 3), "Rear4")
        )
    ));

    defaultSequencer = new Sequencer("Sequencer");
  }


  @Test
  void createNewPickerOnPickerReady() {
    PickerReadyEvent readyEvent = new PickerReadyEvent("Picker");
    readyEvent.accept(warehouse);

    verify(employeeManager).getOrCreatePicker("Picker");
    verify(logger).info("Received picker ready event: {}", readyEvent);
  }

  @Test
  void resetPickerTaskOnReady() {
    Picker picker = new Picker("Picker");
    picker.setTask(request);
    Picker pickerSpy = Mockito.spy(picker);
    when(employeeManager.getOrCreatePicker("Picker")).thenReturn(Optional.of(pickerSpy));

    PickerReadyEvent event = new PickerReadyEvent("Picker");
    warehouse.visit(event);

    verify(pickerSpy).setTask(null);
    verify(picker.getTask().get()).assign(null);
  }

  @Test
  void logFailedPickerCreationOnReadyEvent() {
    when(employeeManager.getOrCreatePicker("Picker")).thenReturn(Optional.empty());
    PickerReadyEvent event = new PickerReadyEvent("Picker");
    warehouse.visit(event);

    verify(logger).error("Could not create picker for PickerReadyEvent {}", event);
  }

  @Test
  void pickerPickEventWithNonExistentPicker() {
    when(employeeManager.getPicker("Picker")).thenReturn(Optional.empty());
    PickerPickEvent event = new PickerPickEvent("Picker", "1");

    warehouse.visit(event);

    verify(employeeManager).getPicker("Picker");

    // New employees should really only be created in response to ready events.
    verify(messageHandler, never()).sendMessage(any(Picker.class), anyString());

    verify(logger).error("Picker {} in event {} does not exist", event.getName(), event.toString());
  }

  @Test
  void pickerPickEventWithoutAssignedTask() {
    Picker picker = Mockito.spy(new Picker("Picker"));
    PickerPickEvent event = new PickerPickEvent("Picker", "some_sku");
    when(employeeManager.getPicker("Picker")).thenReturn(Optional.of(picker));

    verify(messageHandler, never()).sendMessage(any(Picker.class), anyString());
    verify(employeeManager, never()).getOrCreatePicker(anyString());

    warehouse.visit(event);
    verify(logger).error("Picker {} picking without assigned task", picker);
  }

  @Test
  void pickerPickEventWithUnlocatableSku() {
    Picker picker = new Picker("Picker");
    picker.setTask(mock(PickingRequest.class));
    Picker spy = Mockito.spy(picker);
    when(employeeManager.getPicker("Picker")).thenReturn(Optional.of(spy));
    PickerPickEvent event = new PickerPickEvent("Picker", "Can't find this sky");

    warehouse.visit(event);

    verify(logger).error("Pick event for sku without location: {})", event.getSku());
  }

  @Test
  void singleValidPickingEvent() {
    defaultPicker.setTask(request);
    WarehouseLocation location = request.getDirections().get(0).getLocation();
    String sku = request.getDirections().get(0).getSku();
    when(employeeManager.getPicker(defaultPicker.getName())).thenReturn(Optional.of(defaultPicker));
    when(navigator.locationForSku(sku)).thenReturn(Optional.of(location));
    Map<WarehouseLocation, Integer> stock = warehouse.getStock();
    int initialStock = stock.get(location);
    PickerPickEvent event = new PickerPickEvent(defaultPicker.getName(),
        request.getDirections().get(0).getSku());
    warehouse.visit(event);

    assertEquals(initialStock - 1,
        stock.get(location).intValue());

    verify(messageHandler).sendMessage(defaultPicker, String.format("Continue to %1$s for sku %2$s",
        request.getDirections().get(1).getLocation(),
        request.getDirections().get(1).getSku()));
  }

  @Test
  void pickIncorrectSku() {

    defaultPicker.setTask(request);
    when(employeeManager.getPicker("Picker")).thenReturn(Optional.of(defaultPicker));
    when(navigator.locationForSku(anyString()))
        .thenReturn(Optional.of(new WarehouseLocation("A", 0, 0, 0)));
    PickerPickEvent failureEvent = new PickerPickEvent("Picker", "Front3");
    warehouse.visit(failureEvent);

    verify(messageHandler).sendMessage(defaultPicker,
        String.format("Incorrect SKU. Continue to %1$s for %2$s",
            request.getDirections().get(0).getLocation(),
            request.getDirections().get(0).getSku()
        )
    );
  }


  @Test
  void pickThroughToMarshalling() {
    defaultPicker.setTask(request);
    when(navigator.locationForSku(anyString())).thenAnswer(invocation -> {
      String sku = invocation.getArgument(0);
      for (WarehouseDirection direction : request.getDirections()) {
        if (direction.getSku().equals(sku)) {
          return Optional.of(direction.getLocation());
        }
      }
      return Optional.empty();
    });
    when(employeeManager.getPicker(defaultPicker.getName())).thenReturn(Optional.of(defaultPicker));
    List<PickerPickEvent> events = new ArrayList<>();
    for (WarehouseDirection direction : request.getDirections()) {
      events.add(new PickerPickEvent(defaultPicker.getName(), direction.getSku()));
    }
    events.forEach(warehouse::visit);

    verify(messageHandler).sendMessage(defaultPicker, "Proceed to marshalling");
  }

  @Test
  void pickerMarshalling() {
    PickerMarshalingEvent event = new PickerMarshalingEvent(defaultPicker.getName());
    defaultPicker.setTask(request);
    when(employeeManager.getPicker(defaultPicker.getName())).thenReturn(Optional.of(defaultPicker));

    warehouse.visit(event);
    verify(logger).info("Received picker marshaling event: {}", event);
    verify(employeeManager, never()).getOrCreatePicker(anyString());
    verify(request).assign(null);
  }

  @Test
  void nonExistentPickerMarshalling() {
    when(employeeManager.getPicker(anyString())).thenReturn(Optional.empty());
    PickerMarshalingEvent event = new PickerMarshalingEvent(defaultPicker.getName());
    warehouse.visit(event);

    verify(logger).error("Picker {} in event {} does not exist", event.getName(),
        event.toString());
  }

  @Test
  void pickerWithoutTaskMarshalling() {
    when(employeeManager.getPicker(defaultPicker.getName())).thenReturn(Optional.of(defaultPicker));
    PickerMarshalingEvent event = new PickerMarshalingEvent(defaultPicker.getName());
    warehouse.visit(event);

    verify(logger)
        .error("Picker {} at marshalling without assigned task.", defaultPicker.getName());
  }

  @Test
  void sequencerReadyEvent() {
    SequencerReadyEvent event = new SequencerReadyEvent(defaultSequencer.getName());
    warehouse.visit(event);

    verify(logger).info("Received sequencer ready event: {}", event);
    verify(employeeManager).getOrCreateSequencer(defaultSequencer.getName());
  }
}