package exception;

public class DriverNotAvailableException extends Exception {
    public DriverNotAvailableException() {
        super("No driver available for this trip.");
    }

    public DriverNotAvailableException(String message) {
        super(message);
    }
}
