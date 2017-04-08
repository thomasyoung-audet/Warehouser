package warehousesimulator.warehouser.events;

/**
 * Represents a sequencer notifying the system that they are rescanning the skus.
 */
public class SequencerRescanEvent extends EmployeeEvent {

  /**
   * Create a new SequencerRescanEvent.
   *
   * @param name the name of the sequencer rescanning
   */
  public SequencerRescanEvent(String name) {
    super(name);
  }

  /**
   * Have a visitor process this event.
   *
   * @param visitor The visitor to receive this event
   */
  @Override
  public void accept(EventVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "Sequencer " + getName() + " rescanning";
  }
}

