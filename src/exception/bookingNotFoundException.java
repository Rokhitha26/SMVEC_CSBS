package exception;

public class bookingNotFoundException extends Exception {
    public bookingNotFoundException() {
        super("Booking not found.");
    }

    public bookingNotFoundException(String message) {
        super(message);
    }
}
