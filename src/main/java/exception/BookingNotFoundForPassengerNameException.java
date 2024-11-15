package exception;

public class BookingNotFoundForPassengerNameException extends RuntimeException {

    public BookingNotFoundForPassengerNameException(String passangerName) {
        super("No bookings found for passenger " + passangerName + ".");
    }
}
