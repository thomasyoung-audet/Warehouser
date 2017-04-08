package warehousesimulator.simulation;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
class LauncherTest {

  private static Launcher launcher;
  private static TemporaryFolder masterFolder;
  private static String userDir;

  @BeforeAll
  static void setUp() throws IOException {
    launcher = new Launcher();
    masterFolder = new TemporaryFolder();
    masterFolder.create();
    userDir = System.getProperty("user.dir");
  }

  @AfterAll
  static void tearDown() {
    System.setProperty("user.dir", userDir);
    masterFolder.delete();
  }

  @BeforeEach
  void setCwd() throws IOException {
    System.setProperty("user.dir", masterFolder.newFolder().getPath());
  }


  @Test
  void eventsFileExists() {
    launcher.run(new String[]{});
    File eventsFile = new File(System.getProperty("user.dir"), "events.txt");
    assertTrue(eventsFile.exists());
  }

  @Test
  void noArgsRunSimulation() {
    assertTrue(launcher.run(new String[]{}));
  }

  @Test
  void launcherWorksWithNullArgs() {
    launcher.run(null);
  }

  @Test
  void initialStockFileExists() {
    launcher.run(new String[]{});
    File initialFile = new File(System.getProperty("user.dir"), "initial.csv");
    assertTrue(initialFile.exists());
  }

  @Test
  void finalStockFIleExists() {
    launcher.run(new String[]{});
    File finalFile = new File(System.getProperty("user.dir"), "final.csv");
    assertTrue(finalFile.exists());
  }

  @Test
  void ordersFileExists() {
    launcher.run(new String[]{});
    File ordersFile = new File(System.getProperty("user.dir"), "orders.csv");
    assertTrue(ordersFile.exists());
  }

  @Test
  void logFileExists() {
    launcher.run(new String[] {});
    File logFile = new File(System.getProperty("user.dir", "log.txt"));
    assertTrue(logFile.exists());
  }

  @Test
  void customEventsFileAccepted() {
    launcher.run(new String[]{System.getProperty("user.dir") + "/custom_file_name.txt"});
    File eventsFile = new  File(System.getProperty("user.dir"), "custom_file_name.txt");
    assertTrue(eventsFile.exists());
  }

}