package service;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;
import domain.entity.FlightEntity;

import java.util.List;
import java.util.Optional;

public class BookingService {

    private final BookingDao bookingDao;
    private final FlightService flightService;

    public BookingService(BookingDao bookingDao, FlightService flightService) {
        this.bookingDao = bookingDao;
        this.flightService = flightService;
    }

    public boolean addBooking(BookingEntity bookingEntity) {
        if (bookingEntity.getFlightId() != null && bookingEntity.getPassengers() != null
                && !bookingEntity.getPassengers().isEmpty()) {
            return bookingDao.add(bookingEntity);
        }
        return false;
    }

    public Optional<BookingEntity> getBookingById(String id) {
        return bookingDao.getById(id);
    }

    public List<BookingEntity> getAllBookings() {
        return bookingDao.getAll();
    }

    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be null or empty ");
        }
        return bookingDao.findBookingsByPassengerName(passengerName);
    }

    public boolean cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
        return bookingDao.cancelBooking(bookingId);
    }

    public boolean updateBooking(BookingEntity bookingEntity) {
        if (bookingEntity.getId() == null || bookingEntity.getId().isEmpty()) {
            throw new IllegalArgumentException("Flight ID cannot be null or empty");
        }
        return bookingDao.update(bookingEntity);
    }

    public List<BookingEntity> findBookingsByDestination(String destination) throws IllegalAccessException {
        if (destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        List<FlightEntity> flights = flightService.findFlightsByDestination(destination);
        List<String> flightIds = flights.stream()
                .map(FlightEntity::getId)
                .toList();
        return bookingDao.getAll().stream()
                .filter(booking -> flightIds.contains(booking.getFlightId()))
                .toList();
    }

}
