package moduleTest;

import java.util.HashMap;

import exception.BookingNotFoundException;
import exception.VehicleNotFoundException;

public class modules {

    public static HashMap<String, String> vehicles = new HashMap<>();
    public static HashMap<String, String> drivers = new HashMap<>();

    private String assignedVehicle = null;
    private String assignedDriver = null;

    static {
        vehicles.put("Vehicle1", "Available");
        vehicles.put("Vehicle2", "Available");

        drivers.put("Driver1", "Available");
        drivers.put("Driver2", "Available");
    }

    public static void resetAssignments() {
        vehicles.replaceAll((k, v) -> "Available");
        drivers.replaceAll((k, v) -> "Available");
    }

    public boolean bookTrip(String journeyName) {
        // Very basic validation on journey name
        if (journeyName == null || journeyName.isEmpty()) return false;

        // Assign vehicle
        for (String v : vehicles.keySet()) {
            if (vehicles.get(v).equals("Available")) {
                assignedVehicle = v;
                vehicles.put(v, "Scheduled");
                break;
            }
        }

        // Assign driver
        for (String d : drivers.keySet()) {
            if (drivers.get(d).equals("Available")) {
                assignedDriver = d;
                drivers.put(d, "Scheduled");
                break;
            }
        }

        return assignedVehicle != null && assignedDriver != null;
    }

    public String getAssignedVehicle() {
        return assignedVehicle;
    }

    public String getAssignedDriver() {
        return assignedDriver;
    }
    public void VehicleNotFound(String vehicleId) throws VehicleNotFoundException {
        if (!vehicles.containsKey(vehicleId)) {
            throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
        }
    }

    public void BookingNotFound(int bookingId) throws BookingNotFoundException {
        
        if (bookingId != 1 && bookingId != 2) { 
            throw new BookingNotFoundException("Booking with ID " + bookingId + " not found.");
        }
    }
}
