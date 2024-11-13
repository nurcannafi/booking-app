package domain.dao.impl;

import domain.entity.BookingEntity;
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
        BookingEntity booking = new BookingEntity("1", "FL100", List.of("Leo Messi"));
        assertTrue(bookingDao.add(booking));

        Optional<BookingEntity> fetchedBookingOptional = bookingDao.getById("1");
        assertTrue(fetchedBookingOptional.isPresent());
        BookingEntity fetchedBooking = fetchedBookingOptional.get();
        assertEquals("FL100", fetchedBooking.getFlightId());
    }

    @Test
    void testGetAll() {
        bookingDao.add(new BookingEntity("1", "FL100", List.of("Leo Messi")));
        bookingDao.add(new BookingEntity("2", "FL101", List.of("Cristiano Ronaldo")));

        List<BookingEntity> bookings = bookingDao.getAll();
        assertEquals(2, bookings.size());
    }

    @Test
    void testUpdate() {
        BookingEntity booking = new BookingEntity("1", "FL100", List.of("Leo Messi"));
        assertTrue(bookingDao.add(booking));

        booking.setFlightId("FL200");
        assertTrue(bookingDao.update(booking));

        Optional<BookingEntity> updatedBookingOptional = bookingDao.getById("1");
        assertTrue(updatedBookingOptional.isPresent());
        assertEquals("FL200", updatedBookingOptional.get().getFlightId());
    }

    @Test
    void testCancelBooking() {
        bookingDao.add(new BookingEntity("1", "FL100", List.of("Leo Messi")));
        assertTrue(bookingDao.cancelBooking("1"));

        Optional<BookingEntity> cancelledBookingOptional = bookingDao.getById("1");
        assertFalse(cancelledBookingOptional.isPresent());
    }
}
