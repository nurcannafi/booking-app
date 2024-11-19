package controller;

import domain.dao.impl.InMemoryBookingDaoImpl;
import domain.dao.impl.InMemoryFlightDaoImpl;
import domain.entity.BookingEntity;
import domain.entity.FlightEntity;
import domain.entity.PassengerEntity;
import exception.BookingNotFoundException;
import exception.InvalidBookingException;
import model.dto.BookingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookingService;
import service.FlightService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerTests {

    private BookingService bookingService;
    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingService(new InMemoryBookingDaoImpl(), new FlightService(new InMemoryFlightDaoImpl()));
        bookingController = new BookingController(bookingService);
    }

    @Test
    public void testAddBooking_Success() {
        BookingDto bookingDto = new BookingDto(
                null, "flight-id-1", List.of("John,Doe,30", "Jane,Doe,28")
        );

        String result = bookingController.addBooking(bookingDto);
        assertEquals("Booking added successfully.", result);

        List<BookingEntity> bookings = bookingService.getAllBookings();
        assertFalse(bookings.isEmpty());
        assertEquals(2, bookings.get(0).getPassengers().size());
    }

    @Test
    public void testAddBooking_Failure() {
        BookingDto bookingDto = new BookingDto(null, null, List.of());

        String result = bookingController.addBooking(bookingDto);
        assertEquals("Failed to add booking.", result);
    }

    @Test
    public void testGetBookingById_Success() {
        BookingDto bookingDto = new BookingDto(
                null, "flight-id-1", List.of("John,Doe,30")
        );
        bookingController.addBooking(bookingDto);

        Optional<BookingEntity> addedBooking = bookingService.getAllBookings().stream().findFirst();
        assertTrue(addedBooking.isPresent());

        String result = bookingController.getBookingById(addedBooking.get().getId());
        assertNotNull(result);
        assertTrue(result.contains("John"));
    }

    @Test
    public void testGetBookingById_Failure() {
        String result = bookingController.getBookingById("non-exist-id");
        assertEquals("Booking not found.", result);
    }

    @Test
    public void testGetAllBookings() {
        BookingDto bookingDto1 = new BookingDto(
                null, "flight-id-1", List.of("John,Doe,30")
        );

        bookingController.addBooking(bookingDto1);

        List<BookingDto> bookings = bookingController.getAllBookings();
        assertEquals(1, bookings.size());
    }

    @Test
    public void testFindBookingsByPassengerName_Success() {
        BookingDto bookingDto = new BookingDto(
                null, "flight-id-1", List.of("John,Doe,30", "Jane,Doe,28")
        );
        bookingController.addBooking(bookingDto);

        List<BookingDto> bookings = bookingController.findBookingsByPassengerName("John");
        assertEquals(1, bookings.size());
    }

    @Test
    public void testFindBookingsByPassengerName_Failure() {
        InvalidBookingException exception = assertThrows(InvalidBookingException.class, () -> {
            bookingController.findBookingsByPassengerName("");
        });
        assertEquals("Passenger name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testUpdateBooking_Success() {
        BookingDto bookingDto = new BookingDto(
                "abc", "flight-id-1", List.of("John,Doe,30")
        );
        bookingController.addBooking(bookingDto);

        Optional<BookingEntity> addedBooking = bookingService.getAllBookings().stream().findFirst();
        assertTrue(addedBooking.isPresent());

        BookingDto updatedBookingDto = new BookingDto(
                addedBooking.get().getId(), "flight-id-1", List.of("Jane,Doe,28")
        );

        String result = bookingController.updateBooking(updatedBookingDto);
        assertEquals("Booking updated successfully.", result);

        Optional<BookingEntity> updatedBooking = bookingService.getBookingById(addedBooking.get().getId());
        assertEquals(1, updatedBooking.get().getPassengers().size());
        assertEquals("Jane", updatedBooking.get().getPassengers().get(0).getFirstName());
    }

    @Test
    public void testsCancelBooking() {
        BookingDto bookingDto = new BookingDto(
                "abc", "flight-id-1", List.of("John,Doe,30")
        );
        bookingController.addBooking(bookingDto);

        Optional<BookingEntity> addedBooking = bookingService.getAllBookings().stream().findFirst();
        assertTrue(addedBooking.isPresent());

        String result = bookingController.cancelBooking(addedBooking.get().getId());
        assertEquals("Booking canceled successfully.", result);
    }

    @Test
    public void testCancelBooking_Failure() {
        InvalidBookingException exception = assertThrows(InvalidBookingException.class, () -> {
            bookingController.cancelBooking("");
        });
        assertEquals("Booking ID cannot be null or empty", exception.getMessage());
    }
}
