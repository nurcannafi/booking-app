package domain.dao.impl;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDaoImpl implements BookingDao {

    private List<BookingEntity> bookings;

    public BookingDaoImpl() {
        this.bookings = new ArrayList<>();
    }

    @Override
    public boolean add(BookingEntity bookingEntity) {
        return bookings.add(bookingEntity);
    }

    @Override
    public BookingEntity getById(String id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BookingEntity> getAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public boolean update(BookingEntity bookingEntity) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(bookingEntity.getId())) {
                bookings.set(i, bookingEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return bookings.removeIf(booking -> booking.getId().equals(id));
    }

    @Override
    public long count() {
        return bookings.size();
    }

    @Override
    public List<BookingEntity> findBookingsByFlightId(String flightId) {
        return bookings.stream()
                .filter(booking -> booking.getFlightId().equals(flightId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        return bookings.stream()
                .filter(booking -> booking.getPassengerNames().stream()
                        .anyMatch(name -> name.equalsIgnoreCase(passengerName)))
                .collect(Collectors.toList());
    }


    @Override
    public boolean cancelBooking(String bookingId) {
        return delete(bookingId);
    }
}
