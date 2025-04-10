package entity;

import java.time.LocalDateTime;

public class Trips {
    private int tripID;
    private int vehicleID;
    private int routeID;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String status;       // Scheduled, In Progress, Completed, Cancelled
    private String tripType;     // Freight, Passenger
    private int maxPassengers;

    // Constructors
    public Trips() {}

    public Trips(int tripID, int vehicleID, int routeID, LocalDateTime departureDate,
                 LocalDateTime arrivalDate, String status, String tripType, int maxPassengers) {
        this.tripID = tripID;
        this.vehicleID = vehicleID;
        this.routeID = routeID;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.status = status;
        this.tripType = tripType;
        this.maxPassengers = maxPassengers;
    }

    // Getters and Setters
    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    // toString
    @Override
    public String toString() {
        return "Trips{" +
                "tripID=" + tripID +
                ", vehicleID=" + vehicleID +
                ", routeID=" + routeID +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", status='" + status + '\'' +
                ", tripType='" + tripType + '\'' +
                ", maxPassengers=" + maxPassengers +
                '}';
    }
}
