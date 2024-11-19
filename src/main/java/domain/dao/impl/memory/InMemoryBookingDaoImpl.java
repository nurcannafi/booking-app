package domain.dao.impl.memory;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryBookingDaoImpl implements BookingDao {

    private final Map<String, BookingEntity> bookings;

    public InMemoryBookingDaoImpl() {
        this.bookings = new HashMap<>();
    }

    @Override
    public boolean add(BookingEntity bookingEntity) {
        bookings.put(bookingEntity.getId(), bookingEntity);
        return true;
    }

    @Override
    public Optional<BookingEntity> getById(String id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public List<BookingEntity> getAll() {
        return bookings.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean update(BookingEntity bookingEntity) {
        if (bookings.containsKey(bookingEntity.getId())) {
            bookings.put(bookingEntity.getId(), bookingEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return bookings.remove(id) != null;
    }

    @Override
    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        return bookings.values().stream()
                .filter(booking -> booking.getPassengers().stream()
                        .anyMatch(passenger -> passenger.getFirstName().equalsIgnoreCase(passengerName) ||
                                passenger.getLastName().equalsIgnoreCase(passengerName)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        return delete(bookingId);
    }
}
