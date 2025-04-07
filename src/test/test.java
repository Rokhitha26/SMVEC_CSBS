package test;
import java.io.FileNotFoundException;
import entity.Vehicle;
import exception.DriverNotAvailableException;
import exception.bookingNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import dao.TransportService;
import dao.TransportServiceImpl;
import data.RouteMap;
import entity.Bookings;
import entity.Driver;
import entity.Routes;
import entity.Trip;
import entity.Vehicle;

public class test {

	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
	TransportService service= new TransportServiceImpl();
	Scanner get= new Scanner(System.in);
	

	int userType;
	
	do {
		System.out.println("-----TransportManagement-----");
		System.out.println("1.Passenger.");
		System.out.println("2.Admin");
		System.out.println("3.Driver");
		System.out.println("4.Exit App");
		System.out.println();
		System.out.println("Enter your type: ");
		userType=get.nextInt();
		get.nextLine();
		
	switch(userType) {
	
	case 1:
		int passengerChoice;
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter your email: ");
		String email = sc.nextLine().trim();

		System.out.print("Enter your password (4 digits): ");
		String password = sc.nextLine().trim();


		int passengerID = service.validatePassenger(email, password);

		while (passengerID == -1) {
		    System.out.println("Invalid login. Please register or check your credentials.");
			System.out.print("Enter your email: ");
			 email = sc.nextLine().trim();

			System.out.print("Enter your password (4 digits): ");
			 password = sc.nextLine().trim();
		    passengerID=service.validatePassenger(email, password);
		} 
	    
		do {
			System.out.println("-----PassengerDashboard-----");
			System.out.println("1.Book a Trip");
			System.out.println("2.Cancel a Trip");
			System.out.println("3.View booking history");
			System.out.println("4.Exit Passenger Dashboard");
		    System.out.println();
		    System.out.println("Enter your choice");
		    passengerChoice=get.nextInt();
		    get.nextLine();
		    switch(passengerChoice) {
		    case 1:
		    	RouteMap.displayAvailableRoute();
		    	System.out.println("Enter journeyKey");
		    	String journeyKey = get.nextLine();

		    	Routes route = RouteMap.getRouteDetails(journeyKey); // Still get the route object

		    	service.showVehicle();
		    	System.out.println("Enter vehicle");
		    	String vehicle = get.nextLine();
		    	LocalDate departureDate = route.getDepartureDate();

		    	System.out.println("Enter the number of passengers: ");
		    	int passengers = get.nextInt();
		    	get.nextLine();

		    	// Don't create Trip object here
		    	String result = service.scheduleTrip(vehicle, departureDate, passengers, route, passengerID); // just pass data
		    	System.out.println(result);
		    	
		    	break;
		        case 2:
		    	System.out.println("Cancel Trip Dashboard");
		    	System.out.println("Enter you Booked ID: ");
		    	int BookedID=get.nextInt();
		    	boolean res=service.cancelBooking(BookedID);
		    	if(res) {
		    		System.out.println("Bookings Cancelled");
		    	}
		    	else {
		    		System.out.println("Error Occured retry");
		    	}
		    	break;
		    	
		    case 3:
		    	System.out.println("-----BookingHistoryDashboard-----");
		    	List<Bookings>bookingsHistory= service.getBookingsByPassenger(passengerID);
		    	for(Bookings BF: bookingsHistory) {
		    		System.out.println(BF);
		    	}
		    	break;
		    case 4:
		    	System.out.println("Exiting the Passenger DashBoard...");
		    	break;
		    	default:
		    		System.out.println("Enter a valid Choice");
		    		break;	    	
		    }	
		}while(passengerChoice!=4);	
		break;
		
		
	case 2:
		int adminChoice;
		do {
			
			System.out.println("-----AdminDashboard-----");
			System.out.println("1.Add Vehicle to Transport System");
			System.out.println("2.Update Vehicle to Transport System");
			System.out.println("3.Delete Vehicle from Transport System");
			System.out.println("4.Cancel Scheduled Trip");
			System.out.println("5.Get Available drivers");
			System.out.println("6.Get trips data");
			System.out.println("7.Exit Admin Dashboard");
			System.out.println();
			System.out.println("Enter your choice: ");
			adminChoice= get.nextInt();
			get.nextLine();
			
			switch(adminChoice) {
			case 1:
				System.out.println("Add vehicle to TransportSystem");
				System.out.println();
				System.out.println("Enter Vehicle Model: ");
				String model=get.nextLine();
				System.out.println("Enter Vehicle Capacity: ");
				double capacity=get.nextDouble();
				get.nextLine();
				System.out.println("Enter Vehicle Type: ");
				String type=get.nextLine();
				System.out.println("Enter Vehicle Status: ");
				String status=get.nextLine();
				
				boolean vehicleadd=service.addVehicle(new Vehicle(model,capacity,type,status));
				if(vehicleadd) {
					System.out.println("Vehicle added");
				}
				else {
					System.out.println("Error Occured retry");
				}
				break;
				
			case 2:
				System.out.println("Update vehicle to TransportSystem");
				System.out.println("Enter vehicleID :");
				int vehiID=get.nextInt();
				boolean updateResult=service.updateVehicle(vehiID);
				if(updateResult) {
					System.out.println("Vehicle updated");
				}
				else {
					System.out.println("Error Occured");
				}
				break;
				
			case 3:
				System.out.println("Delete vehicle in Transport System");
				System.out.println("Enter the vehicleID: ");
				int deleteID=get.nextInt();
				service.deleteVehicle(deleteID);
				break;
				
			case 4:
				System.out.println("Cancelling a Trip");
				System.out.println("Enter the tripID for the scheduled trip:");
				int cancelID=get.nextInt();
				String cancelResult=service.cancelTrip(cancelID);
				System.out.println(cancelResult);
				break;
				
			case 5:
			    try {
			        System.out.println("Available Drivers");
			        List<String> availableDrivers = service.getAvailableDrivers();
			        
			        if (availableDrivers.isEmpty()) {
			            throw new DriverNotAvailableException("No drivers are currently available.");
			        }

			        for (String displayD : availableDrivers) {
			            System.out.println(displayD);
			        }
			    } catch (DriverNotAvailableException e) {
			        System.out.println("Error: " + e.getMessage());
			    }
			    break;

				
			case 6:
			    System.out.println("Trips data");
			    System.out.println("Enter the tripID");
			    int dataTripID = get.nextInt();
			    try {
			        List<Trip> data = service.getTripsByTripID(dataTripID);
			        if (data.isEmpty()) {
			            throw new bookingNotFoundException("No booking found with trip ID: " + dataTripID);
			        }
			        for (Trip dataResult : data) {
			            System.out.println(dataResult);
			        }
			    } catch (bookingNotFoundException e) {
			        System.out.println("Error: " + e.getMessage());
			    }
			    break;

				
			case 7:
				System.out.println("Exiting Admin Dashboard");
				break;
				
			default:
				System.out.println("Enter a valid option");
				break;		
			}
			
		}while(adminChoice!=7);
		break;
		
	case 3:
		int driverChoice;
		do {
			System.out.println("-----DriverDashboard-----");
			System.out.println();
			System.out.println("1.Driver Status Update");
			System.out.println("2.Cancel Trip");
			System.out.println("3.Exit Driver Dashboard");
			System.out.println();
			System.out.println("Enter your choice: ");
			driverChoice=get.nextInt();
			get.nextLine();
			switch(driverChoice) {
			case 1:
				System.out.println("-----Status Update Dashboard-----");
				System.out.println("Enter the driverID: ");
				int driverIDUpdate=get.nextInt();
				boolean driverS=service.driverStatusUpdate(driverIDUpdate);
				if(driverS) {
					System.out.println("Updated to Available");
				}
				else {
					System.out.println("Error Occured!");
				}
				break;
			case 2:
				System.out.println("-----Cancel a Trip-----");
				System.out.println("Enter the tripID to be deallocate: ");
				int cancelID=get.nextInt();
				String cancelRes=service.cancelTrip(cancelID);
				System.out.println(cancelRes);
			}		
			
		}while(driverChoice!=3);
		
		break;
		
		
	case 4:
		System.out.println("Exiting the Transport System...");
		break;
	
	default:
		System.out.println("Enter a valid choice!!");
	}			
	}while(userType!=4);
	
	}}
	
	
	
	
	
