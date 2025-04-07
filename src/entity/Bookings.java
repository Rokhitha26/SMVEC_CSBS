package entity;

import java.sql.Date;

public class Bookings {

    int bookingID;
    Trip trip;
    Passenger passenger;
    Date bookingDate;
    String status;
    String startDestination;
    String endDestination;

    // Getters
    public int getBookingID() {
        return bookingID;
    }

    public Trip getTrip() {
        return trip;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDestination() {
        return startDestination;
    }

    public String getEndDestination() {
        return endDestination;
    }

    // Setters
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
    }
    
   
    public String toString() {
        return "BookingID: " + bookingID +
               ", Date: " + bookingDate +
               ", Status: " + status +
               ", From: " + startDestination +
               ", To: " + endDestination;
    }

    
    
}
