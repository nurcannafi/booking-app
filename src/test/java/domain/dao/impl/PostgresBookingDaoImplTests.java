package domain.dao.impl;

import domain.dao.impl.database.PostgresBookingDaoImpl;
import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostgresBookingDaoImplTests {

    private PostgresBookingDaoImpl bookingDao;

    @BeforeEach
    void setUp() {
        bookingDao = new PostgresBookingDaoImpl();
    }

    @Test
    void testAddAndGetById() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36, 17);
        PassengerEntity passenger2 = new PassengerEntity("Cristiano", "Ronaldo", 39, 13);

        BookingEntity booking = new BookingEntity("1", "FL100", List.of(passenger1, passenger2));

        assertTrue(bookingDao.add(booking));

        Optional<BookingEntity> fetchedBookingOptional = bookingDao.getById("1");
        assertTrue(fetchedBookingOptional.isPresent());
        BookingEntity fetchedBooking = fetchedBookingOptional.get();

        assertEquals("FL100", fetchedBooking.getFlightId());
        assertEquals(2, fetchedBooking.getPassengers().size());
    }

    @Test
    void testGetAll() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36, 17);
        PassengerEntity passenger2 = new PassengerEntity("Cristiano", "Ronaldo", 39, 13);

        bookingDao.add(new BookingEntity("1", "FL100", List.of(passenger1)));
        bookingDao.add(new BookingEntity("2", "FL101", List.of(passenger2)));

        List<BookingEntity> bookings = bookingDao.getAll();
        assertTrue(bookings.size() >= 2);
    }

    @Test
    void testUpdate() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36, 17);
        BookingEntity booking = new BookingEntity("1", "FL100", List.of(passenger1));
        bookingDao.add(booking);

        booking.setFlightId("FL200");
        assertTrue(bookingDao.update(booking));

        Optional<BookingEntity> updatedBookingOptional = bookingDao.getById("1");
        assertTrue(updatedBookingOptional.isPresent());
        assertEquals("FL200", updatedBookingOptional.get().getFlightId());
    }

    @Test
    void testDelete() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36, 17);
        bookingDao.add(new BookingEntity("1", "FL100", List.of(passenger1)));

        assertTrue(bookingDao.delete("1"));

        assertNull(bookingDao.getById("1"));
    }

    @Test
    void testCancelBooking() {
        PassengerEntity passenger1 = new PassengerEntity("Leo", "Messi", 36, 17);
        bookingDao.add(new BookingEntity("1", "FL100", List.of(passenger1)));

        assertTrue(bookingDao.cancelBooking("1"));

        assertNull(bookingDao.getById("1"));
    }
}

