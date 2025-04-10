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

public class transportManagementServiceImpl implements transportManagementService {

	 @Override
	 public int createPassengerAccount(Scanner get) {
		    System.out.println("-----Create Passenger Account-----");

		    try {
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

		        String sql = "insert into passengers (FirstName, Gender, Age, Email, PhoneNumber) values (?, ?, ?, ?, ?)";

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
		                    System.out.println("\tAccount created Successfully!!!");
		                    return userID; 
		                }
		            }
		            System.out.println("Failed to create account.");
		            return 0;
		        }
		    } catch (Exception e) {
		        System.out.println("An error occurred during account creation. Please try again.");
		        return 0;  
		    }
		}

	 @Override
	 public int loginPassenger(Scanner get) {
		    System.out.println("\t-----Passenger Login-----");

		    try {
		        String email;
		        while (true) {
		            System.out.print("Enter Email (must end with @gmail.com): ");
		            email = get.nextLine().trim();
		            if (email.endsWith("@gmail.com")) break;
		            else System.out.println("Invalid Email! Please use a valid @gmail.com address.");
		        }

		        String sql = "select PassengerID from passengers where Email = ?";

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
		    } catch (Exception e) {
		        System.out.println("An error occurred during login. Please try again.");
		    }

		    return 0;  
		}
	 @Override
	 public void viewAvailableTrips() {
		 
		    String sql = "select startDestination, endDestination, Price, JourneyLabel, TravelDate from routes where TravelDate >= curdate()";

		    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
		         PreparedStatement ps = conn.prepareStatement(sql);
		         ResultSet rs = ps.executeQuery()) {

		        System.out.println("\t\t\t----- Available Trips -----");
		    
		        System.out.printf("\t%-15s %-15s %-10s %-15s %-12s%n",
		                "Start", "End", "Price", "Journey", "Travel Date");

		        System.out.println("\t-------------------------------------------------------------------");

		        while (rs.next()) {
		            String start = rs.getString("StartDestination");
		            String end = rs.getString("EndDestination");
		            double price = rs.getDouble("Price");
		            String journey = rs.getString("JourneyLabel");
		            Date travelDate = rs.getDate("TravelDate");  

		           
		            System.out.printf("\t%-15s %-15s %-10.2f %-15s %-12s%n",
		                    start, end, price, journey, travelDate);
		        }

		    } catch (Exception e) {
		        System.out.println("Error fetching trips");
		    }
		}




	@Override
	public boolean addVehicle(Vehicles vehicle) throws FileNotFoundException, IOException {
	    String sql = "insert into vehicles (model, capacity, type, status) values (?, ?, ?, ?)";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, vehicle.getModel());
	        ps.setDouble(2, vehicle.getCapacity());
	        ps.setString(3, vehicle.getType());
	        ps.setString(4, vehicle.getStatus());

	        int rowsInserted = ps.executeUpdate();
	        return rowsInserted > 0;

	    } catch (Exception e) {
	        System.out.println("Error adding vehicle:");
	       
	        return false;
	    }
	}


	@Override
	public boolean updateVehicle(Vehicles vehicle) throws FileNotFoundException, IOException {
	    String sql = "update vehicles set model = ?, capacity = ?, type = ?, status = ? where vehicleID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        PreparedStatement ps = conn.prepareStatement(sql);

	        ps.setString(1, vehicle.getModel());
	        ps.setDouble(2, vehicle.getCapacity());
	        ps.setString(3, vehicle.getType());
	        ps.setString(4, vehicle.getStatus());
	        ps.setInt(5, vehicle.getVehicleID());

	        int rowsUpdated = ps.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (Exception e) {
	        System.out.println(" Error updating vehicle:");
	      
	        return false;
	    }
	}

	public void deleteVehicle(Scanner scanner) throws IOException, SQLException {
	    System.out.print("Enter Vehicle ID to mark as deleted: ");
	    int vehicleId = scanner.nextInt();
	    scanner.nextLine(); 
	    String sql = "UPDATE vehicles SET Status = 'Deleted' WHERE VehicleID = ?";
	    
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, vehicleId);
	        int rowsUpdated = stmt.executeUpdate();
	        
	        if (rowsUpdated > 0) {
	            System.out.println("Vehicle set to Deleted successfully!");
	        } else {
	            System.out.println("No vehicle found with ID: " + vehicleId);
	        }
	        
	    } catch (Exception e) {
	        System.out.println("Error updating vehicle status ");
	    }
	}

	@Override
	public void bookTrip(Scanner sc, int userID) throws FileNotFoundException, IOException, SQLException {
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // 1. Show available journeys
	        System.out.println("Available Journeys:");
	        viewAvailableTrips(); 

	        // 2. Prompt journey label
	        System.out.print("Enter Journey Label: ");
	        String journeyLabel = sc.nextLine().trim();

	        // 3. Get routeID and travelDate for journeyLabel
	        String routeQuery = "select RouteID, TravelDate from routes where JourneyLabel = ?";
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
	        int numPassengers = sc.nextInt();
	        sc.nextLine();

	        // 5. Find suitable vehicle
	        String vehicleQuery = "select VehicleID, Model from vehicles where Capacity >= ? and Status = 'Available' limit 1";
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
	        try (PreparedStatement ps = conn.prepareStatement("update vehicles set Status = 'Scheduled' where VehicleID = ?")) {
	            ps.setInt(1, vehicleID);
	            ps.executeUpdate();
	        }

	        // 6. Allocate available driver
	        String driverQuery = "select driverID, driverName from driver where dStatus = 'Available' limit 1";
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
	        try (PreparedStatement ps = conn.prepareStatement("update driver set dStatus = 'Scheduled' where driverID = ?")) {
	            ps.setInt(1, driverID);
	            ps.executeUpdate();
	        }

	        // 7. Insert into trips
	        String tripInsert = "insert into trips (VehicleID, RouteID, DepartureDate, Status, MaxPassengers, driverID) values (?, ?, ?, 'Scheduled', ?, ?)";
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

	        String bookingInsert = "insert into bookings (TripID, PassengerID, BookingDate, Status) values (?, ?, ?, 'Booked')";
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
	    catch(Exception e) {
	    	System.out.println("Error on booking a trip");
	    }
	}

	@Override
	public void cancelBooking(Scanner get, int userID) throws FileNotFoundException, IOException {
	    System.out.print("Enter your Booking ID to cancel: ");
	    int bookingID = get.nextInt();
	    get.nextLine();

	    String selectTripQuery = "select TripID from bookings where BookingID = ? and PassengerID = ?";
	    String updateBookingQuery = "update bookings set Status = 'Cancelled' where BookingID = ?";
	    String updateTripStatusQuery = "update trips set Status = 'Cancelled' where TripID = ?";
	    String getVehicleDriverQuery = "select VehicleID, DriverID from trips where TripID = ?";
	    String updateVehicleStatus = "update vehicles set Status = 'Available' where VehicleID = ?";
	    String updateDriverStatus = "update driver set dStatus = 'Available' where driverID = ?";

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
	                    System.out.println("No booking found with the given ID.");
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

	        System.out.println("Booking cancelled successfully.");
	      

	    } catch (Exception e) {
	        System.out.println("Error during trip cancellation ");
	    }
	}

	@Override
	public List<Bookings> getBookingsByPassenger(int passengerId, Scanner get) throws FileNotFoundException, IOException {
	    List<Bookings> bookingsList = new ArrayList<>();

	    String query = "select BookingID, TripID, PassengerID, BookingDate, Status from bookings where PassengerID = ?";

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
	    } catch (Exception e) {
	        System.out.println("Error fetching bookings");
	    }

	    return bookingsList;
	}

	@Override
	public void scheduleTrip(int vehicleId, int routeId, String depDate, String arrDate) {
	    // Date validation
	    LocalDate departureDate;
	    LocalDate arrivalDate;
	    LocalDate today = LocalDate.now();
	    viewAvailableTrips(); 

	    try {
	        departureDate = LocalDate.parse(depDate);
	        arrivalDate = LocalDate.parse(arrDate);
	        
	        // Validate dates
	        if (departureDate.isBefore(today)) {
	            System.out.println("Error: Departure date cannot be in the past");
	            return;
	        }
	        
	        if (arrivalDate.isBefore(departureDate)) {
	            System.out.println("Error: Arrival date must be after departure date");
	            return;
	        }
	    } catch (DateTimeParseException e) {
	        System.out.println("Invalid date format. Please use YYYY-MM-DD");
	        return;
	    }

	    String insertTripSQL = "insert into trips (VehicleID, RouteID, DepartureDate, ArrivalDate, Status, TripType, driverID) values (?, ?, ?, ?, 'Scheduled', 'Freight', ?)";
	    String insertBookingSQL = "insert into bookings (TripID, BookingDate, Status) values (?, ?, 'Admin Booked')";
	    String updateVehicleSQL = "update vehicles SET Status = 'Scheduled' where VehicleID = ?";
	    String updateDriverSQL = "update driver SET dStatus = 'Scheduled' where driverID = ?";
	    String findDriverSQL = "select driverID from driver where dStatus = 'Available' limit 1";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
	        conn.setAutoCommit(false); 

	        // 1. Find available driver
	        int driverId = -1;
	        try (PreparedStatement driverStmt = conn.prepareStatement(findDriverSQL);
	             ResultSet rs = driverStmt.executeQuery()) {
	            if (rs.next()) {
	                driverId = rs.getInt("driverID");
	            } else {
	                System.out.println("Error: No available drivers found");
	                conn.rollback();
	                return;
	            }
	        }

	        // 2. Insert trip with driver assignment
	        int tripId = -1;
	        try (PreparedStatement tripStmt = conn.prepareStatement(insertTripSQL, Statement.RETURN_GENERATED_KEYS)) {
	            tripStmt.setInt(1, vehicleId);
	            tripStmt.setInt(2, routeId);
	            tripStmt.setDate(3, java.sql.Date.valueOf(departureDate));
	            tripStmt.setDate(4, java.sql.Date.valueOf(arrivalDate));
	            tripStmt.setInt(5, driverId);

	            int affectedRows = tripStmt.executeUpdate();
	            if (affectedRows == 0) {
	                throw new SQLException("Creating trip failed, no rows affected.");
	            }

	            try (ResultSet generatedKeys = tripStmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    tripId = generatedKeys.getInt(1);
	                } else {
	                    throw new SQLException("Creating trip failed, no ID obtained.");
	                }
	            }
	        }

	        // 3. Create booking
	        try (PreparedStatement bookingStmt = conn.prepareStatement(insertBookingSQL)) {
	            bookingStmt.setInt(1, tripId);
	            bookingStmt.setDate(2, java.sql.Date.valueOf(departureDate));
	            bookingStmt.executeUpdate();
	        }

	        // 4. Update vehicle status
	        try (PreparedStatement vehicleStmt = conn.prepareStatement(updateVehicleSQL)) {
	            vehicleStmt.setInt(1, vehicleId);
	            vehicleStmt.executeUpdate();
	        }

	        // 5. Update driver status
	        try (PreparedStatement driverStmt = conn.prepareStatement(updateDriverSQL)) {
	            driverStmt.setInt(1, driverId);
	            driverStmt.executeUpdate();
	        }

	        conn.commit(); 
	        
	        System.out.println("Trip scheduled successfully!");
	        System.out.println("Trip ID: " + tripId);
	        System.out.println("Assigned Driver ID: " + driverId);
	     

	    } catch (Exception e) {
	        System.out.println("Error scheduling trip ");
	       
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
	        String checkDate = "select DepartureDate from trips where TripID = ?";
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
	        String updateBookingStatus = "update bookings set Status = 'Cancelled by Management' where TripID = ?";
	        try (PreparedStatement updateBooking = conn.prepareStatement(updateBookingStatus)) {
	            updateBooking.setInt(1, tripId);
	            updateBooking.executeUpdate();
	        }

	        // Step 3: Get VehicleID and DriverID from trips
	        int vehicleId = 0, driverId = 0;
	        String getTripDetails = "select VehicleID, driverID from trips where TripID = ?";
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
	        String updateTripStatus = "update trips set Status = 'Cancelled by Management' where TripID = ?";
	        try (PreparedStatement updateTrip = conn.prepareStatement(updateTripStatus)) {
	            updateTrip.setInt(1, tripId);
	            updateTrip.executeUpdate();
	        }

	        // Step 5: Update vehicle status
	        String updateVehicleStatus = "update vehicles set Status = 'Available' where VehicleID = ?";
	        try (PreparedStatement updateVehicle = conn.prepareStatement(updateVehicleStatus)) {
	            updateVehicle.setInt(1, vehicleId);
	            updateVehicle.executeUpdate();
	        }

	        // Step 6: Update driver status
	        String updateDriverStatus = "update driver set dStatus = 'Available' where driverID = ?";
	        try (PreparedStatement updateDriver = conn.prepareStatement(updateDriverStatus)) {
	            updateDriver.setInt(1, driverId);
	            updateDriver.executeUpdate();
	        }

	        conn.commit(); // All updates successful

	        System.out.println("Trip ID " + tripId + " has been successfully cancelled.");

	    } catch (Exception e) {
	        System.out.println("Error cancelling trip" );

	    }
	}

	@Override
	public List<Bookings> getBookingsByTrip(int tripId) {
	    List<Bookings> bookings = new ArrayList<>();

	    String query = "select * from bookings where TripID = ?";
	    
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
	        System.out.println("Error retrieving bookings");
	    }

	    return bookings;
	}
	public List<String> getAvailableDriverNames() throws FileNotFoundException, IOException {
	    List<String> driverNames = new ArrayList<>();
	    String query = "select driverName from driver where dStatus = 'Available'";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement stmt = conn.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            driverNames.add(rs.getString("driverName"));
	        }

	    } catch (Exception e) {
	        System.out.println("Error on getting available driver names");
	    }

	    return driverNames;
	}

	public void deallocateDriver(Scanner sc) {
	    System.out.print("Enter the Driver ID: ");
	    int driverID = sc.nextInt();

	    String tripStatus = null;
	    String driverStatus = null;
	    int tripID = 0;
	    Date departureDate = null;
	    int vehicleID = 0;

	    String driverQuery = "select dStatus from driver where driverID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement driverStmt = conn.prepareStatement(driverQuery)) {

	        // Step 1: Check if driver exists
	        driverStmt.setInt(1, driverID);
	        ResultSet driverRs = driverStmt.executeQuery();

	        if (!driverRs.next()) {
	            System.out.println("Driver with ID " + driverID + " does not exist.");
	            return;
	        } else {
	            driverStatus = driverRs.getString("dStatus");
	        }

	        // Step 2: Get all trips assigned to this driver
	        String tripQuery = "select TripID, DepartureDate, Status, VehicleID from trips where driverID = ?";
	        PreparedStatement tripStmt = conn.prepareStatement(tripQuery);
	        tripStmt.setInt(1, driverID);
	        ResultSet tripRs = tripStmt.executeQuery();

	        boolean tripFound = false;
	        LocalDate today = LocalDate.now();

	        while (tripRs.next()) {
	            String status = tripRs.getString("Status");
	            Date depDate = tripRs.getDate("DepartureDate");

	            if ("Scheduled".equalsIgnoreCase(status) && depDate.toLocalDate().equals(today)) {
	                tripID = tripRs.getInt("TripID");
	                departureDate = depDate;
	                tripStatus = status;
	                vehicleID = tripRs.getInt("VehicleID");
	                tripFound = true;
	                break;
	            }
	        }

	        if (!tripFound) {
	            System.out.println("No scheduled trip for today assigned to this driver.");
	            return;
	        }

	        // Step 3: Prompt if journey is completed
	        System.out.print("Is the journey completed? (yes/no): ");
	        sc.nextLine(); 
	        String isCompleted = sc.nextLine().trim().toLowerCase();

	        if (isCompleted.equals("yes")) {
	            // Update driver status
	            String updateDriver = "update driver set dStatus = 'Available' where driverID = ?";
	            PreparedStatement updateDriverStmt = conn.prepareStatement(updateDriver);
	            updateDriverStmt.setInt(1, driverID);
	            updateDriverStmt.executeUpdate();

	            // Update trip status
	            String updateTrip = "update trips set Status = 'Completed' where TripID = ?";
	            PreparedStatement updateTripStmt = conn.prepareStatement(updateTrip);
	            updateTripStmt.setInt(1, tripID);
	            updateTripStmt.executeUpdate();

	            // Update vehicle status
	            String updateVehicle = "update vehicles set Status = 'Available' where VehicleID = ?";
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
	public boolean allocateDriver(Scanner sc) {
	    System.out.print("Enter Trip ID: ");
	    int tripId = sc.nextInt();

	    System.out.print("Enter Driver ID: ");
	    int driverId = sc.nextInt();

	    String tripStatus = null;
	    String driverStatus = null;

	    String tripQuery = "select Status from trips where TripID = ?";
	    String driverQuery = "select dStatus from driver where driverID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // Step 1: Check if trip exists and is Scheduled
	        try (PreparedStatement tripStmt = conn.prepareStatement(tripQuery)) {
	            tripStmt.setInt(1, tripId);
	            ResultSet tripRs = tripStmt.executeQuery();

	            if (!tripRs.next()) {
	                System.out.println("Trip with ID " + tripId + " does not exist.");
	                return false;
	            } else {
	                tripStatus = tripRs.getString("Status");
	            }
	        }

	        // Step 2: Check if driver exists and is Available
	        try (PreparedStatement driverStmt = conn.prepareStatement(driverQuery)) {
	            driverStmt.setInt(1, driverId);
	            ResultSet driverRs = driverStmt.executeQuery();

	            if (!driverRs.next()) {
	                System.out.println("Driver with ID " + driverId + " does not exist.");
	                return false;
	            } else {
	                driverStatus = driverRs.getString("dStatus");
	            }
	        }

	        // Step 3: Validate conditions
	        if (!"Scheduled".equalsIgnoreCase(tripStatus)) {
	            System.out.println("Trip is not in Scheduled status.");
	            return false;
	        }

	        if (!"Available".equalsIgnoreCase(driverStatus)) {
	            System.out.println("Driver is not available.");
	            return false;
	        }

	        // Step 4: Allocate driver
	        String updateTrip = "update trips set driverID = ? where TripID = ?";
	        try (PreparedStatement updateStmt = conn.prepareStatement(updateTrip)) {
	            updateStmt.setInt(1, driverId);
	            updateStmt.setInt(2, tripId);
	            int rowsAffected = updateStmt.executeUpdate();

	            if (rowsAffected > 0) {
	                System.out.println("Driver " + driverId + " has been successfully allocated to Trip " + tripId + ".");
	                return true;
	            } else {
	                System.out.println("Failed to allocate driver.");
	                return false;
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Error on allocating driver");
	        return false;
	    }
	}
	

	}
