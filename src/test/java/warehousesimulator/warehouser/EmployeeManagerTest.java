package warehousesimulator.warehouser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class EmployeeManagerTest {

  private EmployeeManager employeeManager;

  @BeforeEach
  void setupEmployeeManager() {
    employeeManager = new EmployeeManager();
  }


  @Test
  void getOrCreatePicker() {
    assertEquals(0, employeeManager.getEmployees().size());
    Optional<Picker> picker = employeeManager.getOrCreatePicker("Steven");
    assertTrue(picker.isPresent());
    assertEquals("Steven", picker.get().getName());
    assertEquals(picker.get(), employeeManager.getOrCreatePicker("Steven").get());
    assertEquals(1, employeeManager.getEmployees().size());
  }

  @Test
  void getOrCreateSequencer() {
    assertEquals(0, employeeManager.getEmployees().size());
    Optional<Sequencer> sequencer = employeeManager.getOrCreateSequencer("Sally");
    assertTrue(sequencer.isPresent());
    assertEquals("Sally", sequencer.get().getName());
    assertEquals(sequencer.get(), employeeManager.getOrCreateSequencer("Sally").get());
    assertEquals(1, employeeManager.getEmployees().size());
  }

  @Test
  void getOrCreateLoader() {
    assertEquals(0, employeeManager.getEmployees().size());
    Optional<Loader> loader = employeeManager.getOrCreateLoader("Larry");
    assertEquals("Larry", loader.get().getName());
    assertEquals(loader.get(), employeeManager.getOrCreateLoader("Larry").get());
    assertEquals(1, employeeManager.getEmployees().size());
  }

  @Test
  void getOrCreateReplenisher() {
    assertEquals(0, employeeManager.getEmployees().size());
    Optional<Replenisher> replenisher = employeeManager.getOrCreateReplenisher("Ruth");
    assertTrue(replenisher.isPresent());
    assertEquals("Ruth", replenisher.get().getName());
    assertEquals(replenisher.get(), employeeManager.getOrCreateReplenisher("Ruth").get());
    assertEquals(1, employeeManager.getEmployees().size());
  }

  @Test
  void attemptToCreateEmployeesWithIdenticalNames() {
    assertEquals(0, employeeManager.getEmployees().size());
    Optional<Picker> pickerPeter = employeeManager.getOrCreatePicker("Peter");
    Optional<Sequencer> sequencerSally = employeeManager.getOrCreateSequencer("Sally");
    Optional<Loader> loaderLarry = employeeManager.getOrCreateLoader("Larry");
    Optional<Replenisher> replenisherRuth = employeeManager.getOrCreateReplenisher("Ruth");
    assertEquals(4, employeeManager.getEmployees().size());

    // Trying to getOrCreate a non-picker employee with the same name as peter should return empty
    // and not add an employee.
    assertFalse(employeeManager.getOrCreateSequencer("Peter").isPresent());
    assertFalse(employeeManager.getOrCreateLoader("Peter").isPresent());
    assertFalse(employeeManager.getOrCreateReplenisher("Peter").isPresent());
    assertEquals(4, employeeManager.getEmployees().size());

    // Same with creating a non-sequencer named Sally
    assertFalse(employeeManager.getOrCreatePicker("Sally").isPresent());
    assertFalse(employeeManager.getOrCreateLoader("Sally").isPresent());
    assertFalse(employeeManager.getOrCreateReplenisher("Sally").isPresent());
    assertEquals(4, employeeManager.getEmployees().size());

    // Or a non-loader named Larry
    assertFalse(employeeManager.getOrCreatePicker("Larry").isPresent());
    assertFalse(employeeManager.getOrCreateSequencer("Larry").isPresent());
    assertFalse(employeeManager.getOrCreateReplenisher("Larry").isPresent());
    assertEquals(4, employeeManager.getEmployees().size());

    // Or a non-replenisher named Ruth
    assertFalse(employeeManager.getOrCreatePicker("Ruth").isPresent());
    assertFalse(employeeManager.getOrCreateSequencer("Ruth").isPresent());
    assertFalse(employeeManager.getOrCreateLoader("Ruth").isPresent());
    assertEquals(4, employeeManager.getEmployees().size());
  }

  @Test
  void getEmployeesByName() {
    Optional<Picker> pickerSteven = employeeManager.getOrCreatePicker("Steven");
    assertTrue(pickerSteven.isPresent());

    Optional<Sequencer> sequencerSally = employeeManager.getOrCreateSequencer("Sally");
    assertTrue(sequencerSally.isPresent());

    Optional<Loader> loaderLarry = employeeManager.getOrCreateLoader("Larry");
    assertTrue(loaderLarry.isPresent());

    Optional<Replenisher> replenisherRuth = employeeManager.getOrCreateReplenisher("Ruth");
    assertTrue(replenisherRuth.isPresent());

    Optional<Employee> optionalSteven = employeeManager.getEmployeeByName("Steven");
    assertTrue(optionalSteven.isPresent());
    assertEquals(pickerSteven.get(), optionalSteven.get());

    Optional<Employee> optionalSally = employeeManager.getEmployeeByName("Sally");
    assertTrue(optionalSally.isPresent());
    assertEquals(sequencerSally.get(), optionalSally.get());

    Optional<Employee> optionalLarry = employeeManager.getEmployeeByName("Larry");
    assertTrue(optionalLarry.isPresent());
    assertEquals(loaderLarry.get(), optionalLarry.get());

    Optional<Employee> optionalRuth = employeeManager.getEmployeeByName("Ruth");
    assertTrue(optionalRuth.isPresent());
    assertEquals(replenisherRuth.get(), optionalRuth.get());


    assertFalse(employeeManager.getEmployeeByName("Not an employee").isPresent());
  }

  @Test
  void getEmployeesByType() {
    Optional<Picker> pickerSteven = employeeManager.getOrCreatePicker("Steven");
    assertTrue(pickerSteven.isPresent());

    Optional<Picker> pickerPeter = employeeManager.getOrCreatePicker("Peter");
    assertTrue(pickerPeter.isPresent());

    Optional<Sequencer> sequencerSally = employeeManager.getOrCreateSequencer("Sally");
    assertTrue(sequencerSally.isPresent());

    Optional<Sequencer> sequencerSue = employeeManager.getOrCreateSequencer("Sue");
    assertTrue(sequencerSue.isPresent());

    Optional<Loader> loaderLarry = employeeManager.getOrCreateLoader("Larry");
    assertTrue(loaderLarry.isPresent());

    Optional<Loader> loaderLana = employeeManager.getOrCreateLoader("Lana");
    assertTrue(loaderLana.isPresent());

    Optional<Replenisher> replenisherRuth = employeeManager.getOrCreateReplenisher("Ruth");
    assertTrue(replenisherRuth.isPresent());

    Optional<Replenisher> replenisherRod = employeeManager.getOrCreateReplenisher("Rod");
    assertTrue(replenisherRod.isPresent());

    List<Picker> pickers = employeeManager.getPickers();
    assertEquals(2, pickers.size());
    assertTrue(pickers.contains(pickerSteven.get()));
    assertTrue(pickers.contains(pickerPeter.get()));

    List<Sequencer> sequencers = employeeManager.getSequencers();
    assertEquals(2, sequencers.size());
    assertTrue(sequencers.contains(sequencerSally.get()));
    assertTrue(sequencers.contains(sequencerSue.get()));

    List<Loader> loaders = employeeManager.getLoaders();
    assertEquals(2, loaders.size());
    assertTrue(loaders.contains(loaderLana.get()));
    assertTrue(loaders.contains(loaderLarry.get()));

    List<Replenisher> replenishers = employeeManager.getReplenishers();
    assertEquals(2, replenishers.size());
    assertTrue(replenishers.contains(replenisherRuth.get()));
    assertTrue(replenishers.contains(replenisherRod.get()));
  }


  @Test
  void getEmployeeTypeMethodsShouldReturnCopies() {
    //All these get methods should return copies
    // to avoid improperly modifying the underlying collection
    List<Picker> pickers = employeeManager.getPickers();

    List<Sequencer> sequencers = employeeManager.getSequencers();

    List<Loader> loaders = employeeManager.getLoaders();

    List<Replenisher> replenishers = employeeManager.getReplenishers();

    Picker fakePicker = new Picker("Fake Picker");
    Sequencer fakeSequencer = new Sequencer("Fake Sequencer");
    Loader fakeLoader = new Loader("Fake Loader");
    Replenisher fakeReplenisher = new Replenisher("Fake Replenisher");

    pickers.add(fakePicker);
    assertEquals(0, employeeManager.getPickers().size());

    sequencers.add(fakeSequencer);
    assertEquals(0, employeeManager.getSequencers().size());

    loaders.add(fakeLoader);
    assertEquals(0, employeeManager.getLoaders().size());

    replenishers.add(fakeReplenisher);
    assertEquals(0, employeeManager.getReplenishers().size());
  }


}