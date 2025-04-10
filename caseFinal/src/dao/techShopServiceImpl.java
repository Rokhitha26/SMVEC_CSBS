package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.Bookings;
import entity.Vehicles;
import util.DBConnUtil;

public class techShopServiceImpl implements techShopService {

	 @Override
	    public int createPassengerAccount(Scanner get) throws SQLException, IOException {
	        System.out.println("-----Create Passenger Account-----");

	        String firstName, gender, email, phone;
	        int age;

	        System.out.print("Enter First Name: ");
	        firstName = get.nextLine();

	        System.out.print("Enter Gender: ");
	        gender = get.nextLine();

	        System.out.print("Enter Age: ");
	        age = get.nextInt();
	        get.nextLine();

	        while (true) {
	            System.out.print("Enter Email (must end with @gmail.com): ");
	            email = get.nextLine();
	            if (email.endsWith("@gmail.com")) break;
	            else System.out.println(" Invalid Email! Please use a valid @gmail.com address.");
	        }

	        System.out.print("Enter Phone Number: ");
	        phone = get.nextLine();

	        String sql = "INSERT INTO passengers (FirstName, Gender, Age, Email, PhoneNumber) VALUES (?, ?, ?, ?, ?)";

	        try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	            ps.setString(1, firstName);
	            ps.setString(2, gender);
	            ps.setInt(3, age);
	            ps.setString(4, email);
	            ps.setString(5, phone);

	            int rows = ps.executeUpdate();

	            if (rows > 0) {
	                ResultSet rs = ps.getGeneratedKeys();
	                if (rs.next()) {
	                    int userID = rs.getInt(1);
	                   
	                    return userID;
	                }
	            } else {
	                System.out.println(" Failed to create account.");
	            }
	        }

	        return -1;
	    }

	 @Override
	 public int loginPassenger(Scanner get) throws SQLException, IOException {
	     System.out.println("-----Passenger Login-----");

	     String email;
	     while (true) {
	         System.out.print("Enter Email (must end with @gmail.com): ");
	         email = get.nextLine();
	         if (email.endsWith("@gmail.com")) break;
	         else System.out.println("Invalid Email! Please use a valid @gmail.com address.");
	     }

	     String sql = "SELECT PassengerID FROM passengers WHERE Email = ?";

	     try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	          PreparedStatement ps = conn.prepareStatement(sql)) {

	         ps.setString(1, email);
	         ResultSet rs = ps.executeQuery();

	         if (rs.next()) {
	             int userID = rs.getInt("PassengerID");
	             System.out.println("Login successful! Welcome back, Passenger ID: " + userID);
	             return userID;
	         } else {
	             System.out.println("Login failed. Email not found.");
	         }
	     }

	     return -1;
	 }

	 @Override
	 public void viewAvailableTrips() {
	     String sql = "SELECT StartDestination, EndDestination, Price, JourneyLabel FROM routes";

	     try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	          PreparedStatement ps = conn.prepareStatement(sql);
	          ResultSet rs = ps.executeQuery()) {

	         System.out.println("----- Available Trips -----");
	         System.out.printf("%-15s %-15s %-10s %-15s%n",
	                 "Start", "End", "Price", "Journey");

	         System.out.println("--------------------------------------------------------------");

	         while (rs.next()) {
	             String start = rs.getString("StartDestination");
	             String end = rs.getString("EndDestination");
	      
	             double price = rs.getDouble("Price");
	             String journey = rs.getString("JourneyLabel");

	             System.out.printf("%-15s %-15s %-10.2f %-15s%n",
	                     start, end, price, journey);
	         }

	     } catch (Exception e) {
	         System.out.println("❌ Error fetching trips: " + e.getMessage());
	     }
	 }



	@Override
	public boolean addVehicle(Vehicles vehicle) throws FileNotFoundException, IOException {
	    String sql = "INSERT INTO vehicles (model, capacity, type, status) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, vehicle.getModel());
	        ps.setDouble(2, vehicle.getCapacity());
	        ps.setString(3, vehicle.getType());
	        ps.setString(4, vehicle.getStatus() == null ? "Available" : vehicle.getStatus()); // default fallback

	        int rowsInserted = ps.executeUpdate();
	        return rowsInserted > 0;

	    } catch (SQLException e) {
	        System.out.println("❌ Error adding vehicle:");
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean updateVehicle(Vehicles vehicle) throws FileNotFoundException, IOException {
	    String sql = "UPDATE vehicles SET model = ?, capacity = ?, type = ?, status = ? WHERE vehicleID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, vehicle.getModel());
	        ps.setDouble(2, vehicle.getCapacity());
	        ps.setString(3, vehicle.getType());
	        ps.setString(4, vehicle.getStatus());
	        ps.setInt(5, vehicle.getVehicleID());

	        int rowsUpdated = ps.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        System.out.println("❌ Error updating vehicle:");
	        e.printStackTrace();
	        return false;
	    }
	}

	public void deleteVehicle(Scanner get) throws FileNotFoundException, IOException {
		String sql="delete from vehicles where vehicleID=?";
		
		try(Connection Conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps= Conn.prepareStatement(sql);
			System.out.println("enter the VehicleId for deletion: ");
			int deleteVehicle= get.nextInt();
			ps.setInt(1, deleteVehicle);
			int updates=ps.executeUpdate();
			if(updates>0) {
			System.out.println("Vehicle deleted from DB!!");}
			else {
				System.out.println("Error Occured. Try again later.");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();	
		}
	}

	@Override
	public void bookTrip(Scanner sc, int userID) throws FileNotFoundException, IOException, SQLException {
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // 1. Show available journeys
	        System.out.println("Available Journeys:");
	        viewAvailableTrips(); // Assuming this prints RouteID + JourneyLabel

	        // 2. Prompt journey label
	        System.out.print("Enter Journey Label: ");
	        String journeyLabel = sc.nextLine().trim();

	        // 3. Get routeID and travelDate for journeyLabel
	        String routeQuery = "SELECT RouteID, TravelDate FROM routes WHERE JourneyLabel = ?";
	        int routeID = 0;
	        LocalDate travelDate = null;

	        try (PreparedStatement ps = conn.prepareStatement(routeQuery)) {
	            ps.setString(1, journeyLabel);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    routeID = rs.getInt("RouteID");
	                    travelDate = rs.getDate("TravelDate").toLocalDate();
	                } else {
	                    System.out.println("Journey not found.");
	                    return;
	                }
	            }
	        }

	        // 4. Prompt number of passengers
	        System.out.print("Enter number of passenger tickets to book: ");
	        int numPassengers = Integer.parseInt(sc.nextLine().trim());

	        // 5. Find suitable vehicle
	        String vehicleQuery = "SELECT VehicleID, Model FROM vehicles WHERE Capacity >= ? AND Status = 'Available' LIMIT 1";
	        int vehicleID = 0;
	        String vehicleModel = null;

	        try (PreparedStatement ps = conn.prepareStatement(vehicleQuery)) {
	            ps.setInt(1, numPassengers);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    vehicleID = rs.getInt("VehicleID");
	                    vehicleModel = rs.getString("Model");
	                } else {
	                    System.out.println("No available vehicle for the given capacity.");
	                    return;
	                }
	            }
	        }

	        // Update vehicle status to Scheduled
	        try (PreparedStatement ps = conn.prepareStatement("UPDATE vehicles SET Status = 'Scheduled' WHERE VehicleID = ?")) {
	            ps.setInt(1, vehicleID);
	            ps.executeUpdate();
	        }

	        // 6. Allocate available driver
	        String driverQuery = "SELECT driverID, driverName FROM driver WHERE dStatus = 'Available' LIMIT 1";
	        int driverID = 0;
	        String driverName = null;

	        try (PreparedStatement ps = conn.prepareStatement(driverQuery);
	             ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                driverID = rs.getInt("driverID");
	                driverName = rs.getString("driverName");
	            } else {
	                System.out.println("No available driver found.");
	                return;
	            }
	        }

	        // Update driver status
	        try (PreparedStatement ps = conn.prepareStatement("UPDATE driver SET dStatus = 'Scheduled' WHERE driverID = ?")) {
	            ps.setInt(1, driverID);
	            ps.executeUpdate();
	        }

	        // 7. Insert into trips
	        String tripInsert = "INSERT INTO trips (VehicleID, RouteID, DepartureDate, Status, MaxPassengers, driverID) VALUES (?, ?, ?, 'Scheduled', ?, ?)";
	        int tripID = 0;
	        try (PreparedStatement ps = conn.prepareStatement(tripInsert, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setInt(1, vehicleID);
	            ps.setInt(2, routeID);
	            ps.setTimestamp(3, Timestamp.valueOf(travelDate.atStartOfDay()));
	            ps.setInt(4, numPassengers);
	            ps.setInt(5, driverID);
	            ps.executeUpdate();

	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) {
	                    tripID = rs.getInt(1);
	                }
	            }
	        }

	        // 8. Insert into bookings
	        int bookingID = -1; // Initialize outside so it's accessible later

	        String bookingInsert = "INSERT INTO bookings (TripID, PassengerID, BookingDate, Status) VALUES (?, ?, ?, 'Booked')";
	        try (PreparedStatement ps = conn.prepareStatement(bookingInsert, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setInt(1, tripID);
	            ps.setInt(2, userID);
	            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

	            int affectedRows = ps.executeUpdate();

	            if (affectedRows > 0) {
	                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        bookingID = generatedKeys.getInt(1);
	                    } else {
	                        System.out.println("Booking inserted but Booking ID could not be retrieved.");
	                    }
	                }
	            } else {
	                System.out.println("Booking insertion failed.");
	            }
	        }

	        // Now print the final trip details with the booking ID
	        System.out.println("Trip booked successfully!");
	        System.out.println("Journey: " + journeyLabel);
	        System.out.println("Vehicle: " + vehicleModel);
	        System.out.println("Driver: " + driverName);
	        System.out.println("Booking ID: " + bookingID);

	    }
	}

	@Override
	public void cancelBooking(Scanner get, int userID) throws FileNotFoundException, IOException {
	    System.out.print("Enter your Booking ID to cancel: ");
	    int bookingID = Integer.parseInt(get.nextLine().trim());

	    String selectTripQuery = "SELECT TripID FROM bookings WHERE BookingID = ? AND PassengerID = ?";
	    String updateBookingQuery = "UPDATE bookings SET Status = 'Cancelled' WHERE BookingID = ?";
	    String updateTripStatusQuery = "UPDATE trips SET Status = 'Cancelled' WHERE TripID = ?";
	    String getVehicleDriverQuery = "SELECT VehicleID, DriverID FROM trips WHERE TripID = ?";
	    String updateVehicleStatus = "UPDATE vehicles SET Status = 'Available' WHERE VehicleID = ?";
	    String updateDriverStatus = "UPDATE driver SET dStatus = 'Available' WHERE driverID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        int tripID = -1;
	        // Step 1: Get the TripID from bookings
	        try (PreparedStatement ps = conn.prepareStatement(selectTripQuery)) {
	            ps.setInt(1, bookingID);
	            ps.setInt(2, userID);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    tripID = rs.getInt("TripID");
	                } else {
	                    System.out.println("No booking found with the given ID for your user.");
	                    return;
	                }
	            }
	        }

	        // Step 2: Cancel the booking
	        try (PreparedStatement ps = conn.prepareStatement(updateBookingQuery)) {
	            ps.setInt(1, bookingID);
	            ps.executeUpdate();
	        }

	        // Step 3: Cancel the trip
	        try (PreparedStatement ps = conn.prepareStatement(updateTripStatusQuery)) {
	            ps.setInt(1, tripID);
	            ps.executeUpdate();
	        }

	        // Step 4: Get VehicleID and DriverID from trips
	        int vehicleID = -1, driverID = -1;
	        try (PreparedStatement ps = conn.prepareStatement(getVehicleDriverQuery)) {
	            ps.setInt(1, tripID);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    vehicleID = rs.getInt("VehicleID");
	                    driverID = rs.getInt("DriverID");
	                }
	            }
	        }

	        // Step 5: Set Vehicle and Driver status to Available
	        try (PreparedStatement ps = conn.prepareStatement(updateVehicleStatus)) {
	            ps.setInt(1, vehicleID);
	            ps.executeUpdate();
	        }

	        try (PreparedStatement ps = conn.prepareStatement(updateDriverStatus)) {
	            ps.setInt(1, driverID);
	            ps.executeUpdate();
	        }

	        System.out.println("Trip and booking cancelled successfully.");
	      //  System.out.println("Vehicle and driver status updated to 'Available'.");

	    } catch (SQLException e) {
	        System.out.println("Error during trip cancellation: " + e.getMessage());
	    }
	}

	@Override
	public List<Bookings> getBookingsByPassenger(int passengerId, Scanner get) throws FileNotFoundException, IOException {
	    List<Bookings> bookingsList = new ArrayList<>();

	    String query = "SELECT BookingID, TripID, PassengerID, BookingDate, Status FROM bookings WHERE PassengerID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, passengerId);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                int bookingID = rs.getInt("BookingID");
	                int tripID = rs.getInt("TripID");
	                LocalDateTime bookingDate = rs.getTimestamp("BookingDate").toLocalDateTime();
	                String status = rs.getString("Status");

	                Bookings booking = new Bookings(bookingID, tripID, passengerId, bookingDate, status);
	                bookingsList.add(booking);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error fetching bookings: " + e.getMessage());
	    }

	    return bookingsList;
	}

	@Override
	public boolean scheduleTrip(int vehicleId, int routeId, String departureDate, String arrivalDate) {
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // Convert date strings to LocalDate
	        LocalDate departure = LocalDate.parse(departureDate.trim());
	        LocalDate arrival = LocalDate.parse(arrivalDate.trim());

	        // Step 1: Check if vehicle is available
	        String checkVehicle = "SELECT Status FROM vehicles WHERE VehicleID = ?";
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkVehicle)) {
	            checkStmt.setInt(1, vehicleId);
	            try (ResultSet rs = checkStmt.executeQuery()) {
	                if (rs.next()) {
	                    String status = rs.getString("Status");
	                    if (!"Available".equalsIgnoreCase(status.trim())) {
	                        System.out.println("Vehicle is not available. Current status: " + status);
	                        return false;
	                    }
	                } else {
	                    System.out.println("Vehicle ID " + vehicleId + " does not exist.");
	                    return false;
	                }
	            }
	        }

	        // Step 2: Insert trip
	        String insertTrip = "INSERT INTO trips (VehicleID, RouteID, DepartureDate, ArrivalDate, Status) VALUES (?, ?, ?, ?, 'Scheduled')";
	        try (PreparedStatement ps = conn.prepareStatement(insertTrip)) {
	            ps.setInt(1, vehicleId);
	            ps.setInt(2, routeId);
	            ps.setDate(3, Date.valueOf(departure));
	            ps.setDate(4, Date.valueOf(arrival));

	            int rowsAffected = ps.executeUpdate();
	            if (rowsAffected > 0) {
	                // Step 3: Update vehicle status to "Scheduled"
	                String updateVehicle = "UPDATE vehicles SET Status = 'Scheduled' WHERE VehicleID = ?";
	                try (PreparedStatement vps = conn.prepareStatement(updateVehicle)) {
	                    vps.setInt(1, vehicleId);
	                    vps.executeUpdate();
	                }

	                System.out.println("Trip scheduled successfully!");
	                return true;
	            } else {
	                System.out.println("Trip scheduling failed.");
	                return false;
	            }
	        }

	    } catch (DateTimeParseException e) {
	        System.out.println("Invalid date format. Use yyyy-MM-dd.");
	        return false;
	    } catch (Exception e) {
	        System.out.println("Error scheduling trip: " + e.getMessage());
	        return false;
	    }
	}

	public void cancelTrip(Scanner get) {
	    System.out.print("Enter the Trip ID to cancel: ");
	    int tripId = Integer.parseInt(get.nextLine().trim());

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // Step 1: Check if the TripID exists in bookings
	        String checkBooking = "SELECT * FROM bookings WHERE TripID = ?";
	        try (PreparedStatement checkStmt = conn.prepareStatement(checkBooking)) {
	            checkStmt.setInt(1, tripId);
	            try (ResultSet rs = checkStmt.executeQuery()) {
	                if (!rs.next()) {
	                    System.out.println("No bookings found for Trip ID: " + tripId);
	                    return;
	                }
	            }
	        }
	     // Step 2: Check if departure date has already passed or is today
	        String checkDate = "SELECT DepartureDate FROM trips WHERE TripID = ?";
	        try (PreparedStatement dateStmt = conn.prepareStatement(checkDate)) {
	            dateStmt.setInt(1, tripId);
	            try (ResultSet rs = dateStmt.executeQuery()) {
	                if (rs.next()) {
	                    LocalDate departureDate = rs.getDate("DepartureDate").toLocalDate();
	                    LocalDate today = LocalDate.now();

	                    if (!departureDate.isAfter(today)) {
	                        System.out.println("Trip cannot be cancelled as it has already started or is scheduled for today.");
	                        return;
	                    }
	                }
	            }
	        }

	        conn.setAutoCommit(false); // Begin transaction

	        // Step 2: Update bookings status
	        String updateBookingStatus = "UPDATE bookings SET Status = 'Cancelled by Management' WHERE TripID = ?";
	        try (PreparedStatement updateBooking = conn.prepareStatement(updateBookingStatus)) {
	            updateBooking.setInt(1, tripId);
	            updateBooking.executeUpdate();
	        }

	        // Step 3: Get VehicleID and DriverID from trips
	        int vehicleId = 0, driverId = 0;
	        String getTripDetails = "SELECT VehicleID, driverID FROM trips WHERE TripID = ?";
	        try (PreparedStatement tripStmt = conn.prepareStatement(getTripDetails)) {
	            tripStmt.setInt(1, tripId);
	            try (ResultSet rs = tripStmt.executeQuery()) {
	                if (rs.next()) {
	                    vehicleId = rs.getInt("VehicleID");
	                    driverId = rs.getInt("driverID");
	                }
	            }
	        }

	        // Step 4: Update trip status
	        String updateTripStatus = "UPDATE trips SET Status = 'Cancelled by Management' WHERE TripID = ?";
	        try (PreparedStatement updateTrip = conn.prepareStatement(updateTripStatus)) {
	            updateTrip.setInt(1, tripId);
	            updateTrip.executeUpdate();
	        }

	        // Step 5: Update vehicle status
	        String updateVehicleStatus = "UPDATE vehicles SET Status = 'Available' WHERE VehicleID = ?";
	        try (PreparedStatement updateVehicle = conn.prepareStatement(updateVehicleStatus)) {
	            updateVehicle.setInt(1, vehicleId);
	            updateVehicle.executeUpdate();
	        }

	        // Step 6: Update driver status
	        String updateDriverStatus = "UPDATE driver SET dStatus = 'Available' WHERE driverID = ?";
	        try (PreparedStatement updateDriver = conn.prepareStatement(updateDriverStatus)) {
	            updateDriver.setInt(1, driverId);
	            updateDriver.executeUpdate();
	        }

	        conn.commit(); // All updates successful

	        System.out.println("Trip ID " + tripId + " has been successfully cancelled.");

	    } catch (Exception e) {
	        System.out.println("Error cancelling trip: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@Override
	public List<Bookings> getBookingsByTrip(int tripId) {
	    List<Bookings> bookings = new ArrayList<>();

	    String query = "SELECT * FROM bookings WHERE TripID = ?";
	    
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, tripId);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Bookings booking = new Bookings();
	                booking.setBookingID(rs.getInt("BookingID"));
	                booking.setTripID(rs.getInt("TripID"));
	                booking.setPassengerID(rs.getInt("PassengerID"));
	                booking.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
	                booking.setStatus(rs.getString("Status"));

	                bookings.add(booking);
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Error retrieving bookings: " + e.getMessage());
	    }

	    return bookings;
	}
	public List<String> getAvailableDriverNames() throws FileNotFoundException, IOException {
	    List<String> driverNames = new ArrayList<>();
	    String query = "SELECT driverName FROM driver WHERE dStatus = 'Available'";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement stmt = conn.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            driverNames.add(rs.getString("driverName"));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return driverNames;
	}

	@Override
	public void updateJourneyStatus(Scanner sc) {
	    
	    System.out.print("Enter the Driver ID: ");
	    int driverID = sc.nextInt();

	    String tripStatus = null;
	    String driverStatus = null;
	    int tripID = 0;
	    Date departureDate = null;
	    int vehicleID = 0;

	    // Step 1: Check if driver exists and get current status
	    String driverQuery = "SELECT dStatus FROM driver WHERE driverID = ?";
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement driverStmt = conn.prepareStatement(driverQuery)) {

	        driverStmt.setInt(1, driverID);
	        ResultSet driverRs = driverStmt.executeQuery();

	        if (!driverRs.next()) {
	            System.out.println("Driver with ID " + driverID + " does not exist.");
	            return;
	        } else {
	            driverStatus = driverRs.getString("dStatus");
	        }

	        // Step 2: Get trip where driver is assigned
	        String tripQuery = "SELECT TripID, DepartureDate, Status, VehicleID FROM trips WHERE driverID = ?";
	        PreparedStatement tripStmt = conn.prepareStatement(tripQuery);
	        tripStmt.setInt(1, driverID);
	        ResultSet tripRs = tripStmt.executeQuery();

	        if (!tripRs.next()) {
	            System.out.println("No trip assigned to this driver.");
	            return;
	        } else {
	            tripID = tripRs.getInt("TripID");
	            departureDate = tripRs.getDate("DepartureDate");
	            tripStatus = tripRs.getString("Status");
	            vehicleID = tripRs.getInt("VehicleID");
	        }

	        // Step 3: Check if status is valid and today is the journey day
	        LocalDate today = LocalDate.now();

	        if (!"Scheduled".equalsIgnoreCase(tripStatus) ||
	            !today.equals(departureDate.toLocalDate())) {

	            System.out.println("Trip is not scheduled for today. Cannot update status.");
	            return;
	        }


	        // Step 4: Prompt user if journey is completed
	        System.out.print("Is the journey completed? (yes/no): ");
	        sc.nextLine(); // consume newline
	        String isCompleted = sc.nextLine().trim().toLowerCase();

	        if (isCompleted.equals("yes")) {
	            // Update driver status
	            String updateDriver = "UPDATE driver SET dStatus = 'Available' WHERE driverID = ?";
	            PreparedStatement updateDriverStmt = conn.prepareStatement(updateDriver);
	            updateDriverStmt.setInt(1, driverID);
	            updateDriverStmt.executeUpdate();

	            // Update trip status
	            String updateTrip = "UPDATE trips SET Status = 'Completed' WHERE TripID = ?";
	            PreparedStatement updateTripStmt = conn.prepareStatement(updateTrip);
	            updateTripStmt.setInt(1, tripID);
	            updateTripStmt.executeUpdate();

	            // Update vehicle status
	            String updateVehicle = "UPDATE vehicles SET Status = 'Available' WHERE VehicleID = ?";
	            PreparedStatement updateVehicleStmt = conn.prepareStatement(updateVehicle);
	            updateVehicleStmt.setInt(1, vehicleID);
	            updateVehicleStmt.executeUpdate();

	            System.out.println("Journey marked as completed successfully.");
	        } else {
	            System.out.println("Journey not marked as completed.");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}





	}
