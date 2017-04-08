package warehousesimulator.util;


import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(JUnitPlatform.class)
public class InputUtilsTest {

    @Test
    void newInputUtils() {
        assertNotNull(new InputUtils());
    }
}
