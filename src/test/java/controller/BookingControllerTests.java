package controller;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;
import exception.InvalidBookingException;
import model.dto.BookingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import service.BookingService;
import service.FlightService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

public class BookingControllerTests {

    @Mock
    private BookingDao bookingDao;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private BookingService bookingService;

    private BookingController bookingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingController = new BookingController(bookingService);
    }

    @Test
    public void testAddBooking_Success() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);
        PassengerEntity passenger2 = new PassengerEntity("John", "Doe", 20, 31);

        BookingDto bookingDto = new BookingDto(null, "flight-id-1", List.of(
                "John,Doe,30,1", "Jane,Doe,28,2"
        ));

        when(bookingDao.add(any())).thenReturn(true);

        String result = bookingController.addBooking(bookingDto);
        assertEquals("Booking added successfully.", result);

        verify(bookingDao, times(1)).add(any());
    }

    @Test
    public void testAddBooking_Failure() {
        BookingDto bookingDto = new BookingDto(null, null, List.of());
        when(bookingDao.add(any())).thenReturn(false);

        String result = bookingController.addBooking(bookingDto);
        assertEquals("Failed to add booking.", result);
    }

    @Test
    public void testGetBookingById_Success() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);

        BookingEntity bookingEntity = new BookingEntity("id-123", "flight-id-1", List.of(passenger1));
        when(bookingDao.getById("id-123")).thenReturn(Optional.of(bookingEntity));

        String result = bookingController.getBookingById("id-123");
        assertNotNull(result);
        assertTrue(result.contains("John"));
        verify(bookingDao, times(1)).getById("id-123");
    }

    @Test
    public void testGetBookingById_Failure() {
        when(bookingDao.getById("non-exist-id")).thenReturn(Optional.empty());

        String result = bookingController.getBookingById("non-exist-id");
        assertEquals("Booking not found.", result);
        verify(bookingDao, times(1)).getById("non-exist-id");
    }

    @Test
    public void testGetAllBookings() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);
        BookingEntity bookingEntity = new BookingEntity("id-123", "flight-id-1", List.of(passenger1));

        when(bookingDao.getAll()).thenReturn(List.of(bookingEntity));

        List<BookingDto> bookings = bookingController.getAllBookings();
        assertEquals(1, bookings.size());
        verify(bookingDao, times(1)).getAll();
    }

    @Test
    public void testFindBookingsByPassengerName_Success() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);
        BookingEntity bookingEntity = new BookingEntity("id-123", "flight-id-1", List.of(passenger1));

        when(bookingDao.findBookingsByPassengerName("John")).thenReturn(List.of(bookingEntity));

        List<BookingDto> bookings = bookingController.findBookingsByPassengerName("John");
        assertEquals(1, bookings.size());
        verify(bookingDao, times(1)).findBookingsByPassengerName("John");
    }

    @Test
    public void testFindBookingsByPassengerName_Failure() {
        InvalidBookingException exception = assertThrows(InvalidBookingException.class, () -> {
            bookingController.findBookingsByPassengerName("");
        });
        assertEquals("Passenger name cannot be null or empty", exception.getMessage());
        verify(bookingDao, never()).findBookingsByPassengerName(any());
    }

    @Test
    public void testUpdateBooking_Success() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);
        BookingEntity bookingEntity = new BookingEntity("id-123", "flight-id-1", List.of(passenger1));

        when(bookingDao.update(any())).thenReturn(true);
        when(bookingDao.getById("id-123")).thenReturn(Optional.of(bookingEntity));

        BookingDto bookingDto = new BookingDto("id-123", "flight-id-1", List.of("John,Doe,30,1"));
        String result = bookingController.updateBooking(bookingDto);
        assertEquals("Booking updated successfully.", result);
        verify(bookingDao, times(1)).update(any());
    }

    @Test
    public void testCancelBooking_Success() {
        PassengerEntity passenger1 = new PassengerEntity("John", "Doe", 20, 30);
        BookingEntity bookingEntity = new BookingEntity("id-123", "flight-id-1", List.of(passenger1));

        when(bookingDao.delete("id-123")).thenReturn(true);

        String result = bookingController.cancelBooking("id-123");
        assertEquals("Booking canceled successfully.", result);
        verify(bookingDao, times(1)).delete("id-123");
    }

    @Test
    public void testCancelBooking_Failure() {
        InvalidBookingException exception = assertThrows(InvalidBookingException.class, () -> {
            bookingController.cancelBooking("");
        });
        assertEquals("Booking ID cannot be null or empty", exception.getMessage());
        verify(bookingDao, never()).delete(any());
    }
}
