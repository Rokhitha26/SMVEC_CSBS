package dao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import entity.Bookings;
import entity.Driver;
import entity.Routes;
import entity.Trip;
import entity.Vehicle;
import exception.DriverNotAvailableException;

public interface TransportService {

	void showVehicle() throws FileNotFoundException, SQLException, IOException;
	boolean addVehicle(Vehicle vehicle) throws FileNotFoundException, IOException;
	boolean updateVehicle(int vehicleID) throws FileNotFoundException, IOException;
	boolean deleteVehicle(int vehicleID) throws FileNotFoundException, IOException;
	String scheduleTrip(String vehicle, LocalDate departureDate, int passengers,Routes route, int passengerID) throws FileNotFoundException, IOException;
	String cancelTrip(int tripID) throws FileNotFoundException, IOException;
	
	
	boolean bookTrip(int passengerID, int tripID);//not done
	boolean cancelBooking(int bookingID);
	int allocateDriver(Connection conn) throws SQLException;
	boolean deallocateDriver(int tripID);
	boolean driverStatusUpdate(int driverID);
    String getDriverNameById(Connection conn, int driverID) throws SQLException;
	
    //int getRouteIDFromRoute(Connection conn);
     int validatePassenger(String email,String password) throws FileNotFoundException, IOException;
	

	void insertBooking(Connection conn, int tripID, int passengerID, LocalDate departureDate) throws SQLException;
	
	List<Bookings>getBookingsByPassenger(int passengerID);
	
	List<Trip> getTripsByTripID(int tripID);
	
	List<String>getAvailableDrivers() throws DriverNotAvailableException;
	
}
