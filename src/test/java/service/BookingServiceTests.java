package service;

import domain.dao.BookingDao;
import domain.dao.impl.InMemoryBookingDaoImpl;
import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;
import exception.InvalidBookingException;
import model.dto.BookingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceTests {

    private BookingService bookingService;
    private BookingDao bookingDao;

    @BeforeEach
    void setUp() {
        bookingDao = new InMemoryBookingDaoImpl();
        bookingService = new BookingService(bookingDao, null);
    }

    @Test
    void testAddBooking_ShouldReturnTrue_WhenValidBookingDto() {
        BookingDto bookingDto = new BookingDto(
                "valid-flight-id", "jdk-11", List.of("John,Doe,30", "Jane,Doe,28"));

        boolean result = bookingService.addBooking(bookingDto);

        assertTrue(result, "Booking should be added successfully.");
    }

    @Test
    void testGetBookingById_ShouldReturnBookingEntity_WhenBookingExists() {
        BookingEntity bookingEntity = new BookingEntity("valid-id", null, List.of());
        bookingDao.add(bookingEntity);

        Optional<BookingEntity> booking = bookingService.getBookingById("valid-id");

        assertTrue(booking.isPresent(), "Booking should be found.");
    }

    @Test
    void testCancelBooking_ShouldReturnTrue_WhenValidBookingId() {
        BookingEntity bookingEntity = new BookingEntity("valid-id", null, List.of());
        bookingDao.add(bookingEntity);

        boolean result = bookingService.cancelBooking("valid-id");

        assertTrue(result, "Booking should be canceled.");
    }

    @Test
    void testCancelBooking_ShouldReturnFalse_WhenInvalidBookingId() {
        boolean result = bookingService.cancelBooking("invalid-id");

        assertFalse(result, "Booking cancellation should fail for invalid ID.");
    }

    @Test
    void testUpdateBooking_ShouldReturnTrue_WhenValidBookingDto() {
        BookingEntity bookingEntity = new BookingEntity("valid-id", null,
                List.of(new PassengerEntity("John", "Doe", 30)));
        bookingDao.add(bookingEntity);

        BookingDto bookingDto = new BookingDto("valid-id", "valid-flight-id", List.of("John,Doe,30"));

        boolean result = bookingService.updateBooking(bookingDto);

        assertTrue(result, "Booking should be updated.");
    }

    @Test
    void testUpdateBooking_ShouldThrowInvalidBookingException_WhenBookingIdIsNull() {
        BookingDto bookingDto = new BookingDto("valid-id", "valid-flight-id", List.of("John,Doe,30"));
        bookingDto.setId(null);

        Exception exception = assertThrows(InvalidBookingException.class, () -> {
            bookingService.updateBooking(bookingDto);
        });

        assertEquals("Booking ID cannot be null or empty", exception.getMessage());
    }
}
