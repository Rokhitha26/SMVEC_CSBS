package dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import entity.Bookings;
import entity.Vehicles;

public interface techShopService {

	int createPassengerAccount(Scanner get) throws SQLException, IOException, IOException;

	int loginPassenger(Scanner get) throws SQLException, IOException;

	void viewAvailableTrips();


	
	void deleteVehicle(Scanner get) throws FileNotFoundException, IOException;

	boolean updateVehicle(Vehicles vehicle) throws FileNotFoundException, IOException;

	boolean addVehicle(Vehicles vehicle) throws FileNotFoundException, IOException;

	void bookTrip(Scanner get, int userID) throws FileNotFoundException, IOException, SQLException;

	void cancelBooking(Scanner get, int userID) throws FileNotFoundException, IOException;

	List<Bookings> getBookingsByPassenger(int passengerId, Scanner get) throws FileNotFoundException, IOException;

	boolean scheduleTrip(int vehicleId, int routeId, String departure, String arrival);

	void cancelTrip(Scanner get);

	List<Bookings> getBookingsByTrip(int tripID);

	List<String> getAvailableDriverNames() throws FileNotFoundException, IOException;

	void updateJourneyStatus(Scanner sc);

	
}

