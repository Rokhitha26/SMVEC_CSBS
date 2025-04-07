package dao;

import entity.Vehicle;
import exception.DriverNotAvailableException;
import entity.Bookings;
import entity.Driver;
import entity.Routes;
import entity.Trip;
import util.DBConnUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TransportServiceImpl implements TransportService {

	Scanner get=new Scanner(System.in);

	public void showVehicle() throws FileNotFoundException, SQLException, IOException {
		String sql= "select model,capacity,type from vehicles where status='Available'";
		
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			Statement st=conn.createStatement();
			ResultSet rs= st.executeQuery(sql);
			
			System.out.printf("%-20s %-10s %-15s%n", "Model", "Capacity", "Type");
			System.out.println("----------------------------------------------------");
			
			while(rs.next()) {
				String model=rs.getString("model");
				double capacity=rs.getDouble("Capacity");
				String type=rs.getString("type");
				
				System.out.printf("%-20s %-10.2f %-15s%n", model, capacity, type);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean addVehicle(Vehicle vehicle) throws FileNotFoundException, IOException {
		String sql="insert into vehicles ( model, capacity,type,status) values(?,?,?,?)";
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps=conn.prepareStatement(sql);
			
			ps.setString(1,vehicle.getModel());
			ps.setDouble(2, vehicle.getCapacity());
			ps.setString(3,vehicle.getType());
			ps.setString(4,vehicle.getStatus());
			
			int rowsInserted=ps.executeUpdate();
			System.out.println("Added vehicle");
			return rowsInserted >0;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean updateVehicle(int vehicleID) throws FileNotFoundException, IOException {
		String sql="update vehicles set model=?, capacity=?, type=?, status=? where vehicleID=?";
		try(Connection conn=DBConnUtil.getConnection("src/util/application.properties")){
			PreparedStatement ps= conn.prepareStatement(sql);
			
			System.out.println("Enter the Model to be updated: ");
			String newModel=get.nextLine();
			ps.setString(1, newModel);
			
			System.out.println("Enter the Updated Capacity: ");
			int newCapacity=get.nextInt();
			get.nextLine();
			ps.setInt(2, newCapacity);
		    
			System.out.println("Enter the Updated Type: ");
			String newType=get.nextLine();
			ps.setString(3,newType);
			
			System.out.println("Enter the updated Status: ");
			String newStatus=get.nextLine();
			ps.setString(4, newStatus);
			
			ps.setInt(5,vehicleID);
			
			int rowsUpdated=ps.executeUpdate();
		  
			return rowsUpdated>0;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	
	}
	
	public boolean deleteVehicle(int vehicleID) throws FileNotFoundException, IOException {
		String sql="delete from vehicles where vehicleID=?";
		
		try(Connection Conn=DBConnUtil.getConnection("src/util/application.properties");){
			PreparedStatement ps= Conn.prepareStatement(sql);
			System.out.println("enter the VehicleId for deletion: ");
			int deleteVehicle= get.nextInt();
			ps.setInt(1, deleteVehicle);
			int updates=ps.executeUpdate();
			System.out.println("Vehicle deleted from DB!!");
			return updates>0;
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String scheduleTrip(String vehicle, LocalDate departureDate, int passengers, Routes route, int passengerID)
	        throws FileNotFoundException, IOException {

	    String insertTripSql = "INSERT INTO trips (vehicleName, departureDate, status, passengerCount, driverID, routeID) VALUES (?, ?, ?, ?, ?, ?)";
	    String driverName;

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        // 1. Get route ID
	        int routeID = getRouteIDFromRoute(conn, route);
	        if (routeID == -1) {
	            return "Failed to save route.";
	        }

	        // 2. Allocate driver
	        int allocatedDriverID = allocateDriver(conn);
	        if (allocatedDriverID == -1) {
	            return "NO available drivers at the moment.";
	        }

	        driverName = getDriverNameById(conn, allocatedDriverID);

	        // 3. Insert into trips
	        PreparedStatement ps = conn.prepareStatement(insertTripSql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, vehicle);
	        ps.setTimestamp(2, Timestamp.valueOf(departureDate.atStartOfDay()));
	        ps.setString(3, "Scheduled");
	        ps.setInt(4, passengers);
	        ps.setInt(5, allocatedDriverID);
	        ps.setInt(6, routeID);

	        int rows = ps.executeUpdate();

	        if (rows > 0) {
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                int tripId = rs.getInt(1); // ✅ get trip ID

	                // ✅ INSERT INTO BOOKINGS here
	                insertBooking(conn, tripId, passengerID, departureDate);

	                return "Trip scheduled Successfully with Driver: " + driverName + " and Trip ID: " + tripId;
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return "Trip Scheduling failed";
	}

	
	public String cancelTrip(int tripID) throws FileNotFoundException, IOException {
	    String updateTripSQL = "UPDATE trips SET tripType = 'Cancelled' WHERE tripID = ?";
	    String updateDriverStatusSQL = "UPDATE drivers SET dstatus = 'Available' WHERE driverID = (SELECT driverID FROM trips WHERE tripID = ?)";
	    String updateBookingsSQL = "UPDATE bookings SET status = 'Cancelled' WHERE tripID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {

	        conn.setAutoCommit(false); // Start transaction

	        // Cancel trip
	        try (PreparedStatement psTrip = conn.prepareStatement(updateTripSQL)) {
	            psTrip.setInt(1, tripID);
	            psTrip.executeUpdate();
	        }

	        // Update driver status
	        try (PreparedStatement psDriver = conn.prepareStatement(updateDriverStatusSQL)) {
	            psDriver.setInt(1, tripID);
	            psDriver.executeUpdate();
	        }

	        // Update bookings table
	        try (PreparedStatement psBooking = conn.prepareStatement(updateBookingsSQL)) {
	            psBooking.setInt(1, tripID);
	            psBooking.executeUpdate();
	        }

	        conn.commit(); // Commit all updates
	        return "Trip cancelled successfully. Driver marked as available, and all related bookings are updated.";

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return "Failed to cancel trip.";
	    }
	}

	
	public boolean bookTrip(int passengerID, int tripID) {
		
		return false;
	}
	
	public boolean cancelBooking(int bookingID) {
		 String updateBookingSQL = "UPDATE bookings SET status = 'Cancelled' WHERE bookingID = ?";
		    String updateTripSQL = "UPDATE trips SET status = 'Cancelled' WHERE tripID = ?";
		    String getTripIdSQL = "SELECT tripID FROM bookings WHERE bookingID = ?";

		    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties")) {
		        // Step 1: Get the tripID for the given bookingID
		        int tripID = -1;
		        try (PreparedStatement ps1 = conn.prepareStatement(getTripIdSQL)) {
		            ps1.setInt(1, bookingID);
		            ResultSet rs = ps1.executeQuery();
		            if (rs.next()) {
		                tripID = rs.getInt("tripID");
		            } else {
		                System.out.println("Booking ID not found.");
		                return false;
		            }
		        }

		        // Step 2: Cancel the booking
		        int bookingRows = 0;
		        try (PreparedStatement ps2 = conn.prepareStatement(updateBookingSQL)) {
		            ps2.setInt(1, bookingID);
		            bookingRows = ps2.executeUpdate();
		        }

		        // Step 3: Cancel the trip
		        int tripRows = 0;
		        try (PreparedStatement ps3 = conn.prepareStatement(updateTripSQL)) {
		            ps3.setInt(1, tripID);
		            tripRows = ps3.executeUpdate();
		        }

		        // Step 4: Check both updates
		        return bookingRows > 0 && tripRows > 0;

		    } catch (SQLException | IOException e) {
		        e.printStackTrace();
		        return false;
		    }
		
		
			}
	
	public int allocateDriver(Connection conn) throws SQLException {
		String sql="Select driverID from drivers where dStatus='Available' limit 1";
		try(Statement st=conn.createStatement();){
			ResultSet rs=st.executeQuery(sql);
			if(rs.next()) {
				int driverID=rs.getInt("driverID");
				
				PreparedStatement dUpdate=conn.prepareStatement("update drivers set dStatus='Assigned' where driverID=?");
				dUpdate.setInt(1,driverID);
				dUpdate.executeUpdate();
				return driverID;
			}
		}
		return -1;
	}
	
	public boolean deallocateDriver(int tripID) {
	    String sql = "UPDATE trips SET driverID = NULL WHERE tripID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, tripID);
	        int rowsUpdated = ps.executeUpdate();

	        return rowsUpdated > 0; 

	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	
	public List<Bookings> getBookingsByPassenger(int passengerID) {
	    List<Bookings> bookingsList = new ArrayList<>();
	    String sql = "SELECT b.bookingID, b.bookingdate, b.status, " +
	                 "r.startdestination, r.enddestination " +
	                 "FROM bookings b " +
	                 "JOIN trips t ON b.tripID = t.tripID " +
	                 "JOIN routes r ON t.routeID = r.routeID " +
	                 "WHERE b.passengerID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, passengerID);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Bookings booking = new Bookings();
	            booking.setBookingID(rs.getInt("bookingID"));
	            booking.setBookingDate(rs.getDate("bookingdate"));
	            booking.setStatus(rs.getString("status"));
	            booking.setStartDestination(rs.getString("startdestination"));
	            booking.setEndDestination(rs.getString("enddestination"));
	            bookingsList.add(booking);
	        }

	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	    }

	    return bookingsList;
	}

	
	public List<Trip> getTripsByTripID(int tripID) {
	    List<Trip> tripList = new ArrayList<>();
	    String sql = "SELECT * FROM trips WHERE tripID = ?";

	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, tripID);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Trip trip = new Trip(
	                rs.getString("vehicleName"),
	                rs.getDate("departureDate").toLocalDate(),
	                rs.getInt("passengerCount")
	            );

	            trip.setTripID(rs.getInt("tripID"));
	            trip.setStatus(rs.getString("status"));
	            trip.setTripType(rs.getString("tripType"));
	            trip.setRouteID(rs.getInt("routeID"));

	            // Optional: Fetch and set Driver, Route, and Vehicle using their respective DAOs if you need full objects
	            tripList.add(trip);
	        }

	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	    }

	    return tripList;
	}

	
	public List<String>getAvailableDrivers()throws DriverNotAvailableException{
	
		 List<String> availableDriverNames = new ArrayList<>();
		    String sql = "SELECT driverName FROM drivers WHERE dstatus = 'Available'";

		    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
		         PreparedStatement ps = conn.prepareStatement(sql);
		         ResultSet rs = ps.executeQuery()) {

		        while (rs.next()) {
		            availableDriverNames.add(rs.getString("driverName"));
		        }
			    
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return availableDriverNames;
	}
	
	public boolean driverStatusUpdate(int driverID) {
		 String updateSQL = "UPDATE drivers SET dstatus = 'Available' WHERE driverID = ?";

		    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
		         PreparedStatement ps = conn.prepareStatement(updateSQL)) {
		        
		        ps.setInt(1, driverID);
		        int rowsUpdated = ps.executeUpdate();

		        return rowsUpdated > 0; 

		    } catch (SQLException | IOException e) {
		        e.printStackTrace();
		        return false;
		    }
		
	}
	
	public String getDriverNameById(Connection conn, int driverID) throws SQLException {
	PreparedStatement ps=conn.prepareStatement("select driverName from drivers where driverID=?");
	ps.setInt(1, driverID);
	ResultSet rs=ps.executeQuery();
	if(rs.next()) {
		return rs.getString("driverName");
	}
	return "Unknown";
	}
	
	
	
	
	public int getRouteIDFromRoute(Connection conn, Routes route) {
	    String insertRouteSql = "INSERT INTO routes (startDestination, endDestination, distance, fare) VALUES (?, ?, ?, ?)";
	    try {
	        PreparedStatement ps = conn.prepareStatement(insertRouteSql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, route.getStartDestination());
	        ps.setString(2, route.getEndDestination());
	        ps.setDouble(3, route.getDistance());
	        ps.setDouble(4, route.getFare());

	        int rows = ps.executeUpdate();
	        if (rows > 0) {
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                return rs.getInt(1); // Return the generated routeID
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // failure
	}
	
	public int validatePassenger(String email, String password) throws FileNotFoundException, IOException {
	    String sql = "SELECT passengerID FROM passengers WHERE email = ? AND password = ?";
	    
	    try (Connection conn = DBConnUtil.getConnection("src/util/application.properties");
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	         
	        ps.setString(1, email.trim());
	        ps.setString(2, password.trim());

	        System.out.println("Trying with email: " + email.trim() + " and password: " + password.trim()); // Debug

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("passengerID");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}

	public void insertBooking(Connection conn, int tripID, int passengerID, LocalDate departureDate) throws SQLException {
	    String insertBookingSQL = "INSERT INTO bookings (tripID, passengerID, bookingdate, status) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement ps = conn.prepareStatement(insertBookingSQL)) {
	        ps.setInt(1, tripID);
	        ps.setInt(2, passengerID);
	        ps.setDate(3, java.sql.Date.valueOf(departureDate));
	        ps.setString(4, "Confirmed"); // or any other status

	        int result = ps.executeUpdate();
	        if (result > 0) {
	            System.out.println("Booking created successfully!");
	        } else {
	            System.out.println("Failed to create booking.");
	        }
	    }
	}










	

}
