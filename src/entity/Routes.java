package entity;
import java.time.LocalDate;

import data.RouteMap;
public class Routes {

	int routeID;
	String startDestination;
	String endDestination;
	double distance;
	double fare;
	LocalDate departureDate;
	
	public Routes(String startDestination, String endDestination, double distance,double fare,LocalDate departureDate){
		//this.routeID=(int)(System.currentTimeMillis() % 1000000000);
		this.startDestination=startDestination;
		this.endDestination=endDestination;
		this.distance=distance;
		this.fare=fare;
		this.departureDate=departureDate;
	}
	
	public int getRouteID() {
		return routeID;
	}
	public String getStartDestination() {
		return startDestination;
	}
	public String getEndDestination() {
		return endDestination;
	}
	public double getDistance() {
		return distance;
	}
	public double getFare() {
		return fare;
	}
	public LocalDate getDepartureDate() {
		return departureDate;
	}
	public void setStartDestination(String startDestination) {
		this.startDestination=startDestination;
	}
	public void setEndDestination(String endDestination) {
		this.endDestination=endDestination;
	}
	public void setDistance(double distance) {
		this.distance=distance;
	}
	public void setFare(double fare) {
		this.fare=fare;
	}
	
	
	public String toString() {
		return "RouteID: "+routeID+
				", Start: "+startDestination+
				",End: "+endDestination+
				", Distance: "+distance+" km"+
				", Fare: "+fare;
		
	}
	
}
