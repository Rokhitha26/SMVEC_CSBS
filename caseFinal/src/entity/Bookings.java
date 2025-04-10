package entity;

import java.time.LocalDateTime;

public class Bookings {
    private int bookingID;
    private int tripID;
    private int passengerID;
    private LocalDateTime bookingDate;
    private String status; // Confirmed, Cancelled, Completed

    public Bookings() {
    	
    }


    public Bookings(int bookingID, int tripID, int passengerID, LocalDateTime bookingDate, String status) {
        this.bookingID = bookingID;
        this.tripID = tripID;
        this.passengerID = passengerID;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getTripID() {
        return tripID;
    }

    public void setTripID(int tripID) {
        this.tripID = tripID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString
    @Override
    public String toString() {
        return "Bookings{" +
                "bookingID=" + bookingID +
                ", tripID=" + tripID +
                ", passengerID=" + passengerID +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                '}';
    }
}
