package warehousesimulator.warehouser;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a single level on a rack in a warehouse.
 */
public class WarehouseLocation implements Comparable<WarehouseLocation> {

  /** The regex used to create locations in string form. */
  public static final Pattern LOCATION_REGEX =
      Pattern.compile("^(?<zone>[A-Za-z]) (?<aisle>\\d+) (?<rack>\\d+) (?<level>\\d+)$");

  /** The zone of this location in the warehouse. */
  private final String zone;

  /** The aisle of this location in the warehouse. */
  private final int aisle;

  /** The rack of this location in the warehouse. */
  private final int rack;

  /** The level on the rock of this location in the warehouse. */
  private final int level;

  /**
   * Create a new {@link WarehouseLocation} at the given zone, aisle, rack and level.
   * 
   * @param zone the zone of the location
   * @param aisle the aisle of the location
   * @param rack the rack of the location
   * @param level the level on the rack of the location
   */
  public WarehouseLocation(String zone, int aisle, int rack, int level) {
    Objects.requireNonNull(zone);
    this.zone = zone;
    this.aisle = aisle;
    this.rack = rack;
    this.level = level;
  }

  /**
   * Create a <code>WarehouseLocation</code> from a string.
   *
   * @param locationString A location string matching {@link WarehouseLocation#LOCATION_REGEX}
   * @return An Optional with the corresponding WarehouseLocation if the string is a valid location
   */
  public static Optional<WarehouseLocation> getLocationFromString(String locationString) {
    Matcher matcher = LOCATION_REGEX.matcher(locationString);
    if (!matcher.matches()) {
      return Optional.empty();
    }
    String zone = matcher.group("zone");
    int aisle = Integer.parseInt(matcher.group("aisle"));
    int rack = Integer.parseInt(matcher.group("rack"));
    int level = Integer.parseInt(matcher.group("level"));

    return Optional.of(new WarehouseLocation(zone, aisle, rack, level));
  }

  /**
   * Get the zone of this location.
   * 
   * @return the string letter of this location's zone.
   */
  public String getZone() {
    return zone;
  }

  /**
   * Get the aisle of this location.
   * 
   * @return the aisle of this location.
   */
  public int getAisle() {
    return aisle;
  }

  /**
   * Get the rack of this location.
   * 
   * @return the rack of this location
   */
  public int getRack() {
    return rack;
  }

  /**
   * Get the level on the rack of this location.
   * 
   * @return The level on the rack of this location
   */
  public int getLevel() {
    return level;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public String toString() {
    return zone + " " + aisle + " " + rack + " " + level;
  }

  /**
   * {@inheritDoc}.
   * 
   * <p>Two {@link WarehouseLocation}s are the same if their zone, aisle, rack and level are the
   * same.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    WarehouseLocation location = (WarehouseLocation) obj;

    return new EqualsBuilder().append(aisle, location.aisle).append(rack, location.rack)
        .append(level, location.level).append(zone, location.zone).build();
  }


  @Override
  public int hashCode() {
    return new HashCodeBuilder(15, 97).append(zone).append(aisle).append(rack).append(level)
        .toHashCode();
  }

  @Override
  public int compareTo(WarehouseLocation other) {
    if (zone.compareTo(other.zone) != 0) {
      return zone.compareTo(other.zone);
    }

    if (aisle != other.aisle) {
      return aisle - other.aisle;
    }

    if (rack != other.rack) {
      return rack - other.rack;
    }

    return level - other.level;
  }
}
