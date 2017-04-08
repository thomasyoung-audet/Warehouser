package warehousesimulator.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import warehousesimulator.warehouser.events.Event;
import warehousesimulator.warehouser.events.EventSubscribable;
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

/**
 * Parses a series of event strings into <code>Events</code>
 *
 * @see warehousesimulator.warehouser.events.Event and its implementors for more details on
 *     warehousesimulator.warehouser.events
 */
public class EventParser implements EventSubscribable {

  private static final Pattern COMMENT_REGEX = Pattern.compile("#.*");
  private static final Pattern ORDER_REGEX = Pattern
      .compile("^Order\\s+(?<model>\\w+)\\s+(?<colour>\\w+)$");
  private static final Pattern PICKER_READY_REGEX = Pattern
      .compile("^Picker\\s(?<name>\\w+)\\s+ready$");
  private static final Pattern PICKER_PICK_REGEX = Pattern
      .compile("^[P|p]icker\\s+(?<name>\\w+)\\s[P|p]ick\\s+(?<sku>\\w+)$");
  private static final Pattern PICKER_MARSHALING_REGEX = Pattern
      .compile("^Picker\\s+(?<name>\\w+)\\s+marshaling$");
  private static final Pattern SEQUENCER_READY_REGEX = Pattern
      .compile("^Sequencer\\s+(?<name>\\w+)\\s+ready$");
  private static final Pattern SEQUENCER_SEQUENCE_REGEX = Pattern.compile(
      "^Sequencer\\s+(?<name>\\w+)\\s+sequences\\s+(?<sku>\\w+)$");
  private static final Pattern SEQUENCER_RESCAN_REGEX = Pattern
      .compile("^Sequencer\\s+(?<name>\\w+)\\s+rescans$");
  private static final Pattern LOADER_READY_REGEX = Pattern
      .compile("^Loader\\s+(?<name>\\w+)\\s+ready$");
  private static final Pattern LOADER_SCAN_REGEX = Pattern
      .compile("^Loader\\s+(?<name>\\w+)\\s+scans\\s+(?<sku>\\w+\\d)$");
  private static final Pattern LOADER_LOAD_REGEX = Pattern
      .compile("^Loader\\s+(?<name>\\w+)\\s+loads$");
  private static final Pattern LOADER_RESCAN_REGEX = Pattern
      .compile("^Loader\\s+(?<name>\\w+)\\s+rescans$");
  private static final Pattern REPLENISHER_READY_REGEX = Pattern
      .compile("(?i:Replenisher)\\s+(?<name>\\w+)\\s+(?i:ready)");
  private static final Pattern REPLENISHER_REPLENISH_REGEX = Pattern.compile(
      "^Replenisher\\s+(?<name>\\w+)\\s+replenish\\s+(?<location>(?<zone>[A|B])\\s+(?<Aisle>[0|1])"
          + "\\s+(?<rack>[0-2])\\s+(?<level>[0-3]))$");

  private static final String GROUP_NAME = "name";

  private final List<Event> events = new ArrayList<>();
  private final Logger logger = LoggerFactory.getLogger(EventParser.class);
  private List<EventVisitor> subscribers = new ArrayList<>();

  /**
   * Create a new EventParser.
   *
   * @param eventStrings a list of Strings to be parsed
   * @throws MalformedEventStringException if any String cannot be parsed as an event
   */
  public EventParser(List<String> eventStrings) throws MalformedEventStringException {
    for (String eventString : eventStrings) {
      if (eventString.isEmpty()) {
        logger.info("Skipping blank line");
        continue;
      }
      if (eventString.charAt(0) == '#') {
        logger.info("Skipping comment line: {}", eventString);
        continue;
      }

      events.add(parseEventString(eventString));
    }
  }

  /**
   * Get all warehousesimulator.warehouser.events parsed by this <Code>EventParser</Code>.
   *
   * @return a copy of this parsers collection of warehousesimulator.warehouser.events as a List
   */
  public List<Event> getEvents() {
    return new ArrayList<>(events);
  }

  private Event parseEventString(String eventString) throws MalformedEventStringException {
    logger.info("Parsing event string: {}", eventString);

    Matcher matcher = ORDER_REGEX.matcher(eventString.trim());
    if (matcher.matches()) {
      return new OrderEvent(matcher.group("model"), matcher.group("colour"));
    }

    matcher = PICKER_READY_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new PickerReadyEvent(matcher.group(GROUP_NAME));
    }

    matcher = PICKER_PICK_REGEX.matcher(eventString);
    if (matcher.matches()) {
      String name = matcher.group(GROUP_NAME);
      String sku = matcher.group("sku");
      return new PickerPickEvent(name, sku);
    }

    matcher = PICKER_MARSHALING_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new PickerMarshalingEvent(matcher.group(GROUP_NAME));
    }

    matcher = SEQUENCER_READY_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new SequencerReadyEvent(matcher.group("name"));
    }

    matcher = SEQUENCER_SEQUENCE_REGEX.matcher(eventString);
    if (matcher.matches()) {
      String name = matcher.group(GROUP_NAME);
      String sku = matcher.group("sku");

      return new SequencerSequenceEvent(name, sku);
    }

    matcher = SEQUENCER_RESCAN_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new SequencerRescanEvent(matcher.group(GROUP_NAME));
    }

    matcher = LOADER_READY_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new LoaderReadyEvent(matcher.group(GROUP_NAME));
    }

    matcher = LOADER_SCAN_REGEX.matcher(eventString);
    if (matcher.matches()) {
      String name = matcher.group(GROUP_NAME);
      String sku = matcher.group("sku");

      return new LoaderScanEvent(name, sku);
    }

    matcher = LOADER_LOAD_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new LoaderLoadEvent(matcher.group(GROUP_NAME));
    }

    matcher = LOADER_RESCAN_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new LoaderRescanEvent(matcher.group(GROUP_NAME));
    }

    matcher = REPLENISHER_READY_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new ReplenisherReadyEvent(matcher.group(GROUP_NAME));
    }

    matcher = REPLENISHER_REPLENISH_REGEX.matcher(eventString);
    if (matcher.matches()) {
      return new ReplenisherReplenishEvent(matcher.group(GROUP_NAME), matcher.group("location"));
    }

    throw new MalformedEventStringException(eventString);
  }

  /**
   * Send all warehousesimulator.warehouser.events to subscribers
   */
  public void sendAllEvents() {
    for (Event e : events) {
      for (EventVisitor subscriber : subscribers) {
        e.accept(subscriber);
      }
    }
  }

  /**
   * Subscribe to this EventParser
   *
   * @param subscriber the object subscribing for future warehousesimulator.warehouser.events
   */
  @Override
  public void subscribe(EventVisitor subscriber) {
    if (!subscribers.contains(subscriber)) {
      subscribers.add(subscriber);
    }
  }
}
