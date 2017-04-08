package warehousesimulator.warehouser.events;

/**
 * A class capable of handling {@link Event}s.
 */
public interface EventVisitor {


  /**
   * Receive a {@link PickerReadyEvent}.
   *
   * @param pickerReadyEvent the event to be processed
   */
  void visit(PickerReadyEvent pickerReadyEvent);

  /**
   * Receive a {@link PickerPickEvent}.
   *
   * @param pickerPickEvent the event to be processed
   */
  void visit(PickerPickEvent pickerPickEvent);

  /**
   * Receive a {@link PickerMarshalingEvent}.
   *
   * @param pickerMarshalingEvent the event to be processed
   */
  void visit(PickerMarshalingEvent pickerMarshalingEvent);

  /**
   * Receive a {@link SequencerReadyEvent}.
   *
   * @param sequencerReadyEvent the event to be processed
   */
  void visit(SequencerReadyEvent sequencerReadyEvent);

  /**
   * Receive a {@link SequencerSequenceEvent}.
   *
   * @param sequencerSequenceEvent the event to be processed
   */
  void visit(SequencerSequenceEvent sequencerSequenceEvent);

  /**
   * Receive a {@link SequencerRescanEvent}.
   *
   * @param sequencerRescanEvent the event to be processed
   */
  void visit(SequencerRescanEvent sequencerRescanEvent);

  /**
   * Receive a {@link LoaderReadyEvent}.
   *
   * @param loaderReadyEvent the event to be processed
   */
  void visit(LoaderReadyEvent loaderReadyEvent);

  /**
   * Receive a {@link LoaderLoadEvent}.
   *
   * @param loaderLoadEvent the event to be processed.
   */
  void visit(LoaderLoadEvent loaderLoadEvent);

  /**
   * Receive a {@link LoaderScanEvent}.
   *
   * @param loaderScanEvent the event to be processed.
   */
  void visit(LoaderScanEvent loaderScanEvent);

  /**
   * Receive a {@link LoaderRescanEvent}.
   *
   * @param loaderRescanEvent the event to be processed
   */
  void visit(LoaderRescanEvent loaderRescanEvent);

  /**
   * Receive a {@link OrderEvent}.
   *
   * @param orderEvent the event to be processed.
   */
  void visit(OrderEvent orderEvent);

  /**
   * Receive a {@link ReplenisherReplenishEvent}.
   *
   * @param replenisherReplenishEvent the event to be processed.
   */
  void visit(ReplenisherReplenishEvent replenisherReplenishEvent);

  /**
   * Receive a {@link ReplenisherReadyEvent}.
   *
   * @param replenisherReadyEvent the event to be processed.
   */
  void visit(ReplenisherReadyEvent replenisherReadyEvent);
}
