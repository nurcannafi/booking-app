package domain.dao;

import domain.entity.BookingEntity;

import java.util.List;
import java.util.Optional;

public interface BookingDao extends Dao<BookingEntity> {

    List<BookingEntity> findBookingsByPassengerName(String passengerName);

    Optional<BookingEntity> getById(String id);

    boolean cancelBooking(String bookingId);
}
