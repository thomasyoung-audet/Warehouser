package warehousesimulator.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import warehousesimulator.warehouser.SkuTranslator;

@RunWith(JUnitPlatform.class)
class CsvDataSkuTranslatorTest {


  @Test
  void translate() throws IOException {

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(getClass().getResourceAsStream("translation/default.csv")))) {

      SkuTranslator translator = new CsvDataSkuTranslator(
          reader.lines().collect(Collectors.toList()));

      List<String> sWhiteSkus = translator.translate("S", "White");
      assertEquals(2, sWhiteSkus.size());
      assertEquals("SW1", sWhiteSkus.get(0));
      assertEquals("SW2", sWhiteSkus.get(1));

      List<String> sesGreenSkus = translator.translate("SES", "Green");
      assertEquals(2, sesGreenSkus.size());
      assertEquals("SESG1", sesGreenSkus.get(0));
      assertEquals("SESG2", sesGreenSkus.get(1));
    }
  }

}