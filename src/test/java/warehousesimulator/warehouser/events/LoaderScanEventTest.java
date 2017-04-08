package warehousesimulator.warehouser.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import warehousesimulator.warehouser.Order;
import warehousesimulator.warehouser.PickingRequest;
import warehousesimulator.warehouser.Loader;

/**
 * Represents a loader scanning the 1 of the 8 skus of two pallets worth of orders, to check if the package
 * matches the expected orders.
 */
@RunWith(JUnitPlatform.class)
public class LoaderScanEventTest {

    private static LoaderScanEvent event;
    private static PickingRequest pickingRequest;

    @BeforeAll
    static void setupClass() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("", "", 1, "front1", "rear1"));
        orders.add(new Order("", "", 2, "front2", "rear2"));
        orders.add(new Order("", "", 3, "front3", "rear3"));
        orders.add(new Order("", "", 4, "front4", "rear4"));

        pickingRequest = new PickingRequest(orders, new ArrayList<>());

        event = new LoaderScanEvent("Paul", "SES1");
    }


    @Test
    void accept() {
        EventVisitor visitor = mock(EventVisitor.class);
        event.accept(visitor);

        verify(visitor).visit(event);
    }

    @Test
    void testToString() {
        assertEquals("Loader Paul scans SES1", event.toString());
    }




}
