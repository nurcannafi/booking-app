package service;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;
import domain.entity.FlightEntity;
import domain.entity.PassengerEntity;
import exception.BookingNotFoundException;
import exception.InvalidBookingException;
import model.dto.BookingDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookingService {

    private final BookingDao bookingDao;
    private final FlightService flightService;

    public BookingService(BookingDao bookingDao, FlightService flightService) {
        this.bookingDao = bookingDao;
        this.flightService = flightService;
    }

    public List<FlightEntity> searchFlights(String destination, String date, int numberOfPeople) {
        List<FlightEntity> allFlights = flightService.getAllFlights();
        return allFlights.stream()
                .filter(flight -> flight.getDestination().equals(destination) && flight.getDepartureTime().equals(date))
                .filter(flight -> flight.getAvailableSeats() >= numberOfPeople)
                .toList();
    }

    public boolean addBooking(BookingDto bookingDto) {
        if (bookingDto.getFlightId() != null && bookingDto.getPassengerNames() != null
                && !bookingDto.getPassengerNames().isEmpty()) {

            List<PassengerEntity> passengers = bookingDto.getPassengerNames().stream()
                    .map(name -> {
                        String[] passengerData = name.split(",");
                        if (passengerData.length == 3) {
                            String firstName = passengerData[0];
                            String lastName = passengerData[1];
                            int age = Integer.parseInt(passengerData[2]);
                            return new PassengerEntity(firstName, lastName, age);
                        }
                        return null;
                    })
                    .filter(passenger -> passenger != null)
                    .toList();

            BookingEntity bookingEntity = new BookingEntity(bookingDto.getFlightId(), passengers);
            return bookingDao.add(bookingEntity);
        }
        return false;
    }

    public Optional<BookingEntity> getBookingById(String id) {
        return Optional.ofNullable(bookingDao.getById(id))
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    public List<BookingEntity> getAllBookings() {
        return bookingDao.getAll();
    }

    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        if (passengerName == null || passengerName.isEmpty()) {
            throw new InvalidBookingException("Passenger name cannot be null or empty");
        }
        return bookingDao.findBookingsByPassengerName(passengerName);
    }

    public boolean cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.isEmpty()) {
            throw new InvalidBookingException("Booking ID cannot be null or empty");
        }
        return bookingDao.cancelBooking(bookingId);
    }

    public boolean updateBooking(BookingDto bookingDto) {
        if (bookingDto.getId() == null || bookingDto.getId().isEmpty()) {
            throw new InvalidBookingException("Booking ID cannot be null or empty");
        }

        List<PassengerEntity> passengers = bookingDto.getPassengerNames().stream()
                .map(name -> {
                    String[] passengerData = name.split(",");
                    if (passengerData.length == 3) {
                        String firstName = passengerData[0];
                        String lastName = passengerData[1];
                        int age = Integer.parseInt(passengerData[2]);
                        return new PassengerEntity(firstName, lastName, age);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        BookingEntity bookingEntity = new BookingEntity(bookingDto.getId(), bookingDto.getFlightId(), passengers);
        return bookingDao.update(bookingEntity);
    }

}
