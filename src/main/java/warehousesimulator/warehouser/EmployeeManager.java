package warehousesimulator.warehouser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages employee {@link Employee employees} within a single {@link Warehouse warehouse}.
 */
public class EmployeeManager {

  private List<Picker> pickerList = new ArrayList<>();
  private List<Sequencer> sequencerList = new ArrayList<>();
  private List<Loader> loaderList = new ArrayList<>();
  private List<Replenisher> replenisherList = new ArrayList<>();

  private final Logger logger = LoggerFactory.getLogger(Employee.class);

  //public boolean haveEmployee(String name) {    this method is the same 
  // as the get employee by name right? except it returns a boolean?
  //  return false;
  //}

  /**
   * Returns a list of all employees entered into the warehouse system.
   *
   * @return list of all employees
   */
  public List<Employee> getEmployees() {
    return Stream.of(pickerList, sequencerList, loaderList, replenisherList)
        .flatMap(Collection::stream).collect(
            Collectors.toList());


  }

  public List<Picker> getPickers() {
    return new ArrayList<>(pickerList);
  }

  public List<Sequencer> getSequencers() {
    return new ArrayList<>(sequencerList);
  }

  public List<Loader> getLoaders() {
    return new ArrayList<>(loaderList);
  }

  public List<Replenisher> getReplenishers() {
    return new ArrayList<>(replenisherList);
  }

  /**
   * Returns the Employee with the given name if it exists.
   *
   * @param name the name of the employee to get
   * @return An Optional containing the employee with thee given name if such an employee exists
   */
  public Optional<Employee> getEmployeeByName(String name) {

    for (Employee e : getEmployees()) {
      if (e.getName().equals(name)) {
        return Optional.of(e);
      }
    }

    return Optional.empty();
  }


  /**
   * Search for a Picker by name.
   *
   * @param name The name of the picker to retrieve.
   * @return An Optional containing the Picker with the given name or Optional.empty() if no such
   *     picker exists
   */
  public Optional<Picker> getPicker(String name) {
    for (Picker p : pickerList) {
      if (p.getName().equals(name)) {
        return Optional.of(p);
      }
    }

    return Optional.empty();
  }

  /**
   * Method checks to see if a picker by the name 'name' exists, and returns him.
   * If no such picker exists, it creates one.
   *
   * @param name name of the picker searched for.
   * @return An Optional containing picker by with the given name or empty if an employee with that
   *     name of another type already exists
   */
  public Optional<Picker> getOrCreatePicker(String name) {

    Optional<Picker> pickerOptional = getPicker(name);

    if (pickerOptional.isPresent()) {
      return pickerOptional;
    }

    for (Employee e : getEmployees()) {
      if (e.getName().equals(name)) {
        return Optional.empty();
      }
    }
    Picker newEmployee = new Picker(name);

    pickerList.add(newEmployee);

    logger.debug("Creating new Picker: {}", newEmployee.getName());
    return Optional.of(newEmployee);
  }

  /**
   * Retrieve a Sequencer by name.
   *
   * @param name name of the sequencer to retrieve
   * @return An Optional containing the sequencer or empty if no such sequencer exists
   */
  Optional<Sequencer> getSequencer(String name) {
    for (Sequencer sequencer : sequencerList) {
      if (sequencer.getName().equals(name)) {
        return Optional.of(sequencer);
      }
    }

    return Optional.empty();
  }

  /**
   * Method checks to see if a sequencer by the name 'name' exists, and returns him.
   * If no such sequencer exists, it creates one.
   *
   * @param name name of the sequencer to get
   * @return a sequencer with the given name or Empty if an employee of another type with the given
   *     name already exists
   */
  public Optional<Sequencer> getOrCreateSequencer(String name) {
    Optional<Sequencer> sequencerOptional = getSequencer(name);

    if (sequencerOptional.isPresent()) {
      return sequencerOptional;
    }

    for (Employee e : getEmployees()) {
      if (e.getName().equals(name)) {
        return Optional.empty();
      }
    }

    Sequencer newEmployee = new Sequencer(name);
    sequencerList.add(newEmployee);
    logger.debug("Creating new Sequencer: {}", newEmployee.getName());
    return Optional.of(newEmployee);
  }


  /**
   * Retrieve a Loader name.
   *
   * @param name the name of the Loader to retrieve
   * @return An optional containing the Loader or Optional.empty if no such loader exists
   */
  public Optional<Loader> getLoader(String name) {
    for (Loader loader : loaderList) {
      if (loader.getName().equals(name)) {
        return Optional.of(loader);
      }
    }

    return Optional.empty();
  }

  /**
   * Method checks to see if a loader by the name 'name' exists, and returns him.
   * If no such loader exists, it creates one.
   *
   * @param name name of the loader searched for.
   * @return a loader with the given name or empty if an employee of another type with the name
   *     already exists
   */
  public Optional<Loader> getOrCreateLoader(String name) {

    Optional<Loader> loaderOptional = getLoader(name);
    if (loaderOptional.isPresent()) {
      return loaderOptional;
    }

    for (Employee e : getEmployees()) {
      if (e.getName().equals(name)) {
        return Optional.empty();
      }
    }

    Loader newEmployee = new Loader(name);
    loaderList.add(newEmployee);
    logger.debug("Creating new Loader: {}", newEmployee.getName());
    return Optional.of(newEmployee);
  }

  /**
   * Retrieve a Replenisher by name.
   *
   * @param name the name of the replenisher to retrieve
   * @return An Optional containing the replenisher or Optional.empty if no such Replenisher exists
   */
  public Optional<Replenisher> getReplenisher(String name) {
    for (Replenisher replenisher : replenisherList) {
      if (replenisher.getName().equals(name)) {
        return Optional.of(replenisher);
      }
    }

    return Optional.empty();
  }

  /**
   * Method checks to see if a replenisher by the name 'name' exists, and returns him.
   * If no such replenisher exists, it creates one.
   *
   * @param name name of the replenisher searched for.
   * @return a replenisher with the given name or empty if an employee of another type with that
   *     name already exists
   */
  public Optional<Replenisher> getOrCreateReplenisher(String name) {
    Optional<Replenisher> replenisherOptional = getReplenisher(name);
    if (replenisherOptional.isPresent()) {
      return replenisherOptional;
    }

    for (Employee e : getEmployees()) {
      if (e.getName().equals(name)) {
        return Optional.empty();
      }
    }
    Replenisher newEmployee = new Replenisher(name);
    replenisherList.add(newEmployee);
    logger.debug("Creating new Replenisher: {}", newEmployee.getName());
    return Optional.of(newEmployee);
  }

  /**
   * Get all Pickers that have no current task.
   *
   * @return pickers that have no currently assigned @{@link PickingRequest}
   */
  public List<Picker> getReadyPickers() {
    return pickerList.stream().filter((Picker::isReady)).collect(Collectors.toList());
  }

}
