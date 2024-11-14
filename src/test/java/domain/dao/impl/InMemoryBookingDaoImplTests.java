package domain.dao.impl;

import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryBookingDaoImplTests {

    private InMemoryBookingDaoImpl bookingDao;

    @BeforeEach
    void setUp() {
        bookingDao = new InMemoryBookingDaoImpl();
    }

    @Test
    void testAddAndGetById() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36);

        BookingEntity booking = new BookingEntity("1", "FL100", List.of(passenger1));

        assertTrue(bookingDao.add(booking));

        Optional<BookingEntity> fetchedBookingOptional = bookingDao.getById("1");
        assertTrue(fetchedBookingOptional.isPresent());
        BookingEntity fetchedBooking = fetchedBookingOptional.get();

        assertEquals("FL100", fetchedBooking.getFlightId());
        assertEquals(1, fetchedBooking.getPassengers().size());
    }

    @Test
    void testGetAll() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36);
        PassengerEntity passenger2 = new PassengerEntity("Cristiano", "Ronaldo", 39);

        bookingDao.add(new BookingEntity("1", "FL100", List.of(passenger1)));
        bookingDao.add(new BookingEntity("2", "FL101", List.of(passenger2)));

        List<BookingEntity> bookings = bookingDao.getAll();
        assertEquals(2, bookings.size());
    }

    @Test
    void testUpdate() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36);
        BookingEntity booking = new BookingEntity("1", "FL100", List.of(passenger1));
        assertTrue(bookingDao.add(booking));

        booking.setFlightId("FL200");
        assertTrue(bookingDao.update(booking));

        Optional<BookingEntity> updatedBookingOptional = bookingDao.getById("1");
        assertTrue(updatedBookingOptional.isPresent());
        assertEquals("FL200", updatedBookingOptional.get().getFlightId());
    }

    @Test
    void testCancelBooking() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36);
        bookingDao.add(new BookingEntity("1", "FL100", List.of(passenger1)));

        assertTrue(bookingDao.cancelBooking("1"));

        Optional<BookingEntity> cancelledBookingOptional = bookingDao.getById("1");
        assertFalse(cancelledBookingOptional.isPresent());
    }
}
