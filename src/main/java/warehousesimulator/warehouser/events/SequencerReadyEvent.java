package warehousesimulator.warehouser.events;

/**
 * Represents a sequencer notifying the system that they are ready to work.
 */
public final class SequencerReadyEvent extends EmployeeEvent {

  /**
   * Create a new SequencerReadyEvent.
   *
   * @param name the name of the newly ready sequencer
   */
  public SequencerReadyEvent(String name) {
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
    return "Sequencer " + getName() + " ready";
  }
}
