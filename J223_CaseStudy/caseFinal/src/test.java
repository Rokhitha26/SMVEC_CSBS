import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exception.BookingNotFoundException;
import exception.VehicleNotFoundException;
import moduleTest.modules;

class test {

    static modules m;

    @BeforeAll
    static void setup() {
        m = new modules();
    }

    @BeforeEach
    void resetBeforeEach() {
        modules.resetAssignments(); 
    }
    @Test
    void testBookingSuccessful() {
        boolean isBooked = m.bookTrip("journey3");
        assertTrue(isBooked, "1");
    }
    @Test
    void testDriverMappedToVehicleSuccessfully() {
        boolean result = m.bookTrip("journey1");
        assertTrue(result, "1");

        String assignedDriver = m.getAssignedDriver();
        String assignedVehicle = m.getAssignedVehicle();

        assertNotNull(assignedDriver, "1");
        assertNotNull(assignedVehicle, "1");

       assertEquals("Scheduled", modules.drivers.get(assignedDriver));
        assertEquals("Scheduled", modules.vehicles.get(assignedVehicle));
    }

    @Test
    void testVehicleAndDriverAssignedSuccessfully() {
        boolean isBooked = m.bookTrip("journey2");
        assertTrue(isBooked, "Trip should be booked");

        assertNotNull(m.getAssignedVehicle(), "1");
        assertNotNull(m.getAssignedDriver(), "1");
  
    }   
    @Test
    void testVehicleNotFoundExceptionThrown() {
        Exception exception = assertThrows(VehicleNotFoundException.class, () -> {
            m.VehicleNotFound("NonExistentVehicle");
        });

        String expectedMessage = "Vehicle with ID NonExistentVehicle not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testBookingNotFoundExceptionThrown() {
        Exception exception = assertThrows(BookingNotFoundException.class, () -> {
            m.BookingNotFound(999); 
        });

        String expectedMessage = "Booking with ID 999 not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}


