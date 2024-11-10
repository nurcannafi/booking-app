package domain.dao;

import domain.entity.BookingEntity;

import java.util.List;

public interface BookingDao extends Dao<BookingEntity> {

    List<BookingEntity> findBookingsByFlightId(String flightId);

    List<BookingEntity> findBookingsByPassengerName(String passengerName);

    boolean cancelBooking(String bookingId);
}
