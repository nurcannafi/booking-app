package exception;

public class BookingNotFoundException extends RuntimeException{

    public BookingNotFoundException(String bookingId) {
        super("Booking with ID " + bookingId + " not found.");
    }
}
