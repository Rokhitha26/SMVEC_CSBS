package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import dao.transportManagementService;
import dao.transportManagementServiceImpl;
import entity.Bookings;
import entity.Vehicles;

public class main {

    public static void main(String[] args) throws SQLException, IOException {
        Scanner get = new Scanner(System.in);
        transportManagementService service = new transportManagementServiceImpl();

        int userType = 0;
        int userID = 0;
        

        do {
            System.out.println("\t-----Transport Management-----");
            System.out.println();
            System.out.println("\t1. Passenger");
            System.out.println("\t2. Admin");
            System.out.println("\t3. Driver");
            System.out.println("\t4. Exit Transport System");
            System.out.println();
            System.out.print("Enter your Choice: ");
            userType = get.nextInt();
            get.nextLine();

            switch (userType) {
                case 1:
                    int passengerMenuChoice = 0;
                    boolean isLoggedIn = false;
                    do {
                        System.out.println("\t-----Passenger Dashboard-----");
                        System.out.println("\t1. Create Account");
                        System.out.println("\t2. Login");
                        System.out.print("Enter your choice: ");
                        passengerMenuChoice = get.nextInt();
                        get.nextLine();

                        switch (passengerMenuChoice) {
                            case 1:
                              userID=  service.createPassengerAccount(get);
                              isLoggedIn = true;
                                break;
                            case 2:
                                userID = service.loginPassenger(get);
                                if (userID != 0) {
                                    isLoggedIn = true;
                                   
                                } else {
                                    System.out.println("\tLogin failed. Please try again.");
                                }
                                break;
                            default:
                                System.out.println("\tInvalid choice. Please try again.");
                        }
                    } while (!isLoggedIn);

                    int userChoice = 0;
                    do {
                        System.out.println("\t-----Passenger Dashboard Options-----");
                        System.out.println("\t1. View available trips");
                        System.out.println("\t2. Book a Trip");
                        System.out.println("\t3. Cancel Trip");
                        System.out.println("\t4. Get Bookings");
                        System.out.println("\t5. Exit Dashboard");
                        System.out.print("Enter the option: ");
                        userChoice = get.nextInt();
                        get.nextLine();

                        switch (userChoice) {
                            case 1:
                               service.viewAvailableTrips();
                                break;
                            case 2:
                            	System.out.println("\t-----Book a Trip-----");
                            	System.out.println();
                            	service.bookTrip(get,userID);
                               System.out.println();
                                break;
                            case 3:
                            	System.out.println("\t------Cancel Booking------");
                                service.cancelBooking(get, userID);
                                System.out.println();
                                break;
                            case 4:
                            List<Bookings> bookingsFromPassenger=service.getBookingsByPassenger(userID, get);
                            if (bookingsFromPassenger.isEmpty()) {
                                System.out.println("No bookings found");
                            } else {
                                System.out.println("\nBookings History");
                                System.out.println();
                                for (Bookings b : bookingsFromPassenger) {
                                    System.out.println("Booking ID: " + b.getBookingID());
                                    System.out.println("Passenger ID: " + b.getPassengerID());
                                    System.out.println("Booking Date: " + b.getBookingDate());
                                    System.out.println("Status: " + b.getStatus());
                                    System.out.println("-----------------------------");
                                }
                            }
                                break;
                            case 5:
                                System.out.println("Exiting Passenger Dashboard...");
                                break;
                            default:
                                System.out.println("Invalid choice. Try again.");
                        }
                    } while (userChoice != 5);
                    break;

                case 2:
                	int adminMenuChoice=0;
                    System.out.println("\t-----Admin Dashboard-----");
                    System.out.println();
                    do {
                    	System.out.println("\t1. Add Vehicle to Transport System.");
                    	System.out.println("\t2.Update details of Existing Vehicles");
                    	System.out.println("\t3.Delete vehicles from system");
                    	System.out.println("\t4.Schedule a trip");
                    	System.out.println("\t5.Cancel a trip");
                    	System.out.println("\t6.Get bookings by trip ");
                    	System.out.println("\t7.Get available drivers");
                    	System.out.println("\t8.Exit Transport System");
                    	System.out.println();
                    	System.out.println("Enter your choice: ");
                    	adminMenuChoice=get.nextInt();
                    	get.nextLine();
                    	
                    	switch(adminMenuChoice) {
                    	case 1:
                    		System.out.println("\t-----Add Vehicle-----");
                    		System.out.print("Enter Model: ");
                    		String model = get.nextLine();
                    		System.out.print("Enter Capacity: ");
                    		double capacity = get.nextDouble();
                    		get.nextLine();
                    		System.out.print("Enter Type (Truck, Van, Bus,Car): ");
                    		String type = get.nextLine();
                    		System.out.print("Enter Status (Available, On Trip, Maintenance): ");
                    		String status = get.nextLine();

                    		Vehicles newVehicle = new Vehicles(model, capacity, type, status);
                    		boolean result = service.addVehicle(newVehicle);

                    		if (result) {
                    		    System.out.println("Vehicle added successfully.");
                    		} else {
                    		    System.out.println("Failed to add vehicle.");
                    		}
                            System.out.println();
                    		break;
                    	case 2:
                    		System.out.println("\t-----Update Vehicle-----");
                    		System.out.print("Enter Vehicle ID: ");
                    		int vehicleIDU = get.nextInt();
                    		get.nextLine();

                    		System.out.print("Enter New Model: ");
                    		String modelU = get.nextLine();
                    		System.out.print("Enter New Capacity: ");
                    		double capacityU = get.nextDouble();
                    		get.nextLine();
                    		System.out.print("Enter New Type (Truck, Van, Bus): ");
                    		String typeU = get.nextLine();
                    		System.out.print("Enter New Status (Available, On Trip, Maintenance): ");
                    		String statusU = get.nextLine();

                    		Vehicles updatedVehicle = new Vehicles(vehicleIDU, modelU, capacityU, typeU, statusU);
                    		boolean updateResult = service.updateVehicle(updatedVehicle);

                    		if (updateResult) {
                    		    System.out.println(" Vehicle updated successfully.");
                    		} else {
                    		    System.out.println(" Update failed.");
                    		}
                             System.out.println();
                    		break;
                    		
                    	case 3:
                    		System.out.println("\t-----Delete Vehicles-----");
                    	service.deleteVehicle(get); 
                    		//service.deleteVehicle2(get);
                    		System.out.println();
                    		break;
                    	case 4:
                    		System.out.println("\t-----Schedule a Trip-----");
                    		System.out.println();
                    		
                    		System.out.print("Enter vehicle ID: ");
                    		int vehicleId = Integer.parseInt(get.nextLine().trim());

                    		System.out.print("Enter route ID: ");
                    		int routeId = Integer.parseInt(get.nextLine().trim());

                    		System.out.print("Enter departure date (yyyy-MM-dd): ");
                    		String depDate = get.nextLine().trim();

                    		System.out.print("Enter arrival date (yyyy-MM-dd): ");
                    		String arrDate = get.nextLine().trim();

                    		service.scheduleTrip(vehicleId, routeId, depDate, arrDate);

                    		System.out.println();
                    		break;
                    		
                    	case 5:
                    		System.out.println("\t-----Cancel a trip-----");
                    		service.cancelTrip(get);
                    		System.out.println();
                    		break;
                    		
                    	case 6:
                    		System.out.println("\t-----Get bookings by trip-----");
                    		System.out.println();
                    		System.out.println("enter the tripID: ");
                    		int tripID=get.nextInt();
                    		get.nextLine();
                    		List<Bookings>bookingsByTrip=service.getBookingsByTrip(tripID);
                    		if (bookingsByTrip.isEmpty()) {
                    		    System.out.println("No bookings found for TripID: " + tripID);
                    		} else {
                    		    System.out.println("Bookings for TripID " + tripID + ":");
                    		    for (Bookings booking : bookingsByTrip) {
                    		        System.out.println(booking); // uses your toString() method
                    		    }
                    		}
                    		System.out.println();
                    		break;
                    	case 7:
                    		System.out.println("\t-----Get Available Drivers-----");
                    		List<String> availableDriver=service.getAvailableDriverNames();
                    		if (availableDriver.isEmpty()) {
                    		    System.out.println("No drivers are currently available.");
                    		} else {
                    		    for (String name : availableDriver) {
                    		        System.out.println(name);
                    		    }
                    		}
                    		System.out.println();
                    		break;
                    	case 8:
                    		System.out.println("\tExiting Admin DashBoard...");
                    		break;
                    		default:
                    			System.out.println("\tEnter a valid choice");
                    			
                    	}
                    
                    }while(adminMenuChoice!=8);
                    
                    break;

                case 3:
                    System.out.println("\t-----Driver Dashboard-----");
                    service.deallocateDriver(get);
                    break;

                case 4:
                    System.out.println("\tExiting Transport System...");
                    break;

                default:
                    System.out.println("\tEnter a valid option");
            }

        } while (userType != 4);

        get.close();
    }
}
