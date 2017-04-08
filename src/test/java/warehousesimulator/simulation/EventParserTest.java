package warehousesimulator.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import warehousesimulator.warehouser.events.Event;
import warehousesimulator.warehouser.events.EventVisitor;
import warehousesimulator.warehouser.events.LoaderLoadEvent;
import warehousesimulator.warehouser.events.LoaderReadyEvent;
import warehousesimulator.warehouser.events.OrderEvent;
import warehousesimulator.warehouser.events.PickerMarshalingEvent;
import warehousesimulator.warehouser.events.PickerPickEvent;
import warehousesimulator.warehouser.events.PickerReadyEvent;
import warehousesimulator.warehouser.events.ReplenisherReadyEvent;
import warehousesimulator.warehouser.events.ReplenisherReplenishEvent;
import warehousesimulator.warehouser.events.SequencerReadyEvent;
import warehousesimulator.warehouser.events.SequencerSequenceEvent;

@RunWith(JUnitPlatform.class)
class EventParserTest {

  private static final String ORDER = "Order SES Blue";
  private static final String ALICE_READY = "Picker Alice ready";
  private static final String ALICE_PICK_ONE = "Picker Alice pick 1";
  private static final String LARGE_NUMBER_PICK = "Picker Biggs pick 123900";
  private static final String ALICE_MARSHALING = "Picker Alice marshaling";
  private static final String SUE_READY = "Sequencer Sue ready";
  private static final String SUE_SEQUENCE_ONE_EIGHT = "Sequencer Sue sequences 1";
  private static final String BILL_READY = "Loader Bill ready";
  private static final String BILL_LOAD = "Loader Bill loads";
  private static final String RUBY_REPLENISH = "Replenisher Ruby replenish A 0 0 0";
  private static final String RUBY_READY = "Replenisher Ruby ready";
  private static final String PICK_NEGATIVE_SKU = "Picker Failure pick -1";
  private static final String NON_EVENT = "This is not even close to a valid event";


  @Test
  void emptyEventList() throws MalformedEventStringException {
    EventParser parser = new EventParser(new ArrayList<>());

    assertEquals(0, parser.getEvents().size());

  }

  @Test
  void singleOrder() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(ORDER));
    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof OrderEvent);
    OrderEvent oe = (OrderEvent) events.get(0);
    assertEquals("SES", oe.getModel());
    assertEquals("Blue", oe.getColour());
  }

  @Test
  void singlePickerReady() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(ALICE_READY));

    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof PickerReadyEvent);
    assertEquals("Alice", ((PickerReadyEvent) events.get(0)).getName());
  }

  @Test
  void singlePick() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(ALICE_PICK_ONE));

    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof PickerPickEvent);
    PickerPickEvent ppe = (PickerPickEvent) events.get(0);
    assertEquals("Alice", ppe.getName());
    assertEquals("1", ppe.getSku());
  }

  @Test
  void largerNumberPick() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(LARGE_NUMBER_PICK));
    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof PickerPickEvent);
    PickerPickEvent ppe = (PickerPickEvent) events.get(0);
    assertEquals("123900", ppe.getSku());
    assertEquals("Biggs", ppe.getName());
  }

  @Test
  void singlePickerMarshaling() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(ALICE_MARSHALING));
    List<Event> events = parser.getEvents();

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof PickerMarshalingEvent);
    PickerMarshalingEvent pme = (PickerMarshalingEvent) events.get(0);
    assertEquals("Alice", pme.getName());
  }

  @Test
  void singleSequencerReady() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(SUE_READY));
    List<Event> events = parser.getEvents();

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof SequencerReadyEvent);
    SequencerReadyEvent sre = (SequencerReadyEvent) events.get(0);

    assertEquals("Sue", sre.getName());
  }

  @Test
  void singleSequencerSequences() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(SUE_SEQUENCE_ONE_EIGHT));
    List<Event> events = parser.getEvents();

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof SequencerSequenceEvent);
    SequencerSequenceEvent sse = (SequencerSequenceEvent) events.get(0);
    assertEquals("Sue", sse.getName());

    String sku = sse.getSku();
    assertEquals("1", sku);
  }

  @Test
  void singleLoaderReady() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(BILL_READY));
    List<Event> events = parser.getEvents();

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof LoaderReadyEvent);
    assertEquals("Bill", ((LoaderReadyEvent) events.get(0)).getName());
  }

  @Test
  void singleLoaderLoad() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(BILL_LOAD));
    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof LoaderLoadEvent);
    assertEquals("Bill", ((LoaderLoadEvent) events.get(0)).getName());
  }

  @Test
  void singleReplenisherReady() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(RUBY_READY));
    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof ReplenisherReadyEvent);
    assertEquals("Ruby", ((ReplenisherReadyEvent) events.get(0)).getName());
  }

  @Test
  void singleReplenisherReplenish() throws MalformedEventStringException {
    EventParser parser = new EventParser(Collections.singletonList(RUBY_REPLENISH));
    List<Event> events = parser.getEvents();
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof ReplenisherReplenishEvent);
    ReplenisherReplenishEvent rre = (ReplenisherReplenishEvent) events.get(0);
    assertEquals("Ruby", rre.getName());
    assertEquals("A 0 0 0", rre.getLocation());
  }

  @Test
  void negativeSkuFailure() throws MalformedEventStringException {
    MalformedEventStringException e = assertThrows(MalformedEventStringException.class,
        () -> new EventParser(Collections.singletonList(PICK_NEGATIVE_SKU)));
    assertEquals(PICK_NEGATIVE_SKU, e.getEventString());
  }

  @Test
  void nonEventFailure() throws MalformedEventStringException {
    MalformedEventStringException e = assertThrows(MalformedEventStringException.class,
        () -> new EventParser(Collections.singletonList(NON_EVENT)));
    assertEquals(NON_EVENT, e.getEventString());
  }

  @Test
  void maintainEventOrder() throws MalformedEventStringException {
    List<String> eventsList = Arrays.asList(
        ORDER,
        ALICE_READY,
        ALICE_PICK_ONE,
        SUE_READY,
        RUBY_READY,
        ALICE_MARSHALING,
        SUE_SEQUENCE_ONE_EIGHT,
        BILL_READY,
        BILL_LOAD,
        RUBY_REPLENISH);

    EventParser parser = new EventParser(eventsList);
    List<Event> events = parser.getEvents();
    assertEquals(10, events.size());
    assertTrue(events.get(0) instanceof OrderEvent);
    assertTrue(events.get(1) instanceof PickerReadyEvent);
    assertTrue(events.get(2) instanceof PickerPickEvent);
    assertTrue(events.get(3) instanceof SequencerReadyEvent);
    assertTrue(events.get(4) instanceof ReplenisherReadyEvent);
    assertTrue(events.get(5) instanceof PickerMarshalingEvent);
    assertTrue(events.get(6) instanceof SequencerSequenceEvent);
    assertTrue(events.get(7) instanceof LoaderReadyEvent);
    assertTrue(events.get(8) instanceof LoaderLoadEvent);
    assertTrue(events.get(9) instanceof ReplenisherReplenishEvent);
  }

  @Test
  void eventParserTakesSubscribers() throws MalformedEventStringException {
    List<String> eventList = Arrays.asList(
        ORDER,
        ALICE_READY,
        SUE_READY
    );
    EventVisitor visitor = mock(EventVisitor.class);
    EventParser parser = new EventParser(eventList);
    parser.subscribe(visitor);
    parser.sendAllEvents();

    ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
    verify(visitor).visit(any(OrderEvent.class));
    verify(visitor).visit(any(PickerReadyEvent.class));
    verify(visitor).visit(any(SequencerReadyEvent.class));


  }


}