package domain.service;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;
import domain.exception.InvalidFlightOperationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightService {

    private final FlightDao flightDao;

    public FlightService(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    public boolean addFlight(FlightEntity flightEntity) {
        if (flightEntity.getAvailableSeats() > 0 &&
                flightEntity.getDestination() != null &&
                flightEntity.getDepartureLocation() != null &&
                flightEntity.getDepartureTime() != null) {
            return flightDao.add(flightEntity);
        }
        return false;
    }

    public Optional<FlightEntity> getFlightById(String id) {
        return flightDao.getById(id);
    }

    public List<FlightEntity> getAllFlights() {
        return flightDao.getAll();
    }

    public List<FlightEntity> findFlightsByDestination(String destination) {
        if (destination == null || destination.isEmpty()) {
            throw new InvalidFlightOperationException("Destination cannot be null or empty");
        }
        return flightDao.findFlightsByDestination(destination);
    }

    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        if (minimumSeats < 1) {
            throw new InvalidFlightOperationException("Minimum seats must be at least 1");
        }
        return flightDao.findAvailableFlights(minimumSeats);
    }

    public boolean updateFlight(FlightEntity flight) {
        if (flight.getId() == null || flight.getId().isEmpty()) {
            throw new InvalidFlightOperationException("Flight ID cannot be null or empty");
        }
        return flightDao.update(flight);
    }

    public boolean deleteFlight(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidFlightOperationException("Flight ID cannot be null or empty");
        }
        return flightDao.delete(id);
    }

    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        if (newAvailableSeats < 0) {
            throw new InvalidFlightOperationException("Available seats cannot be negative");
        }
        return flightDao.updateAvailableSeats(flightId, newAvailableSeats);
    }

    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        if (departureLocation == null || departureLocation.isEmpty()) {
            throw new InvalidFlightOperationException("Departure location cannot be null or empty");
        }
        return flightDao.findFlightsByDepartureLocation(departureLocation);
    }

    public List<FlightEntity> searchFlights(String destination, String date, int numberOfPassengers) {
        LocalDate searchDate = LocalDate.parse(date);
        return flightDao.getAll().stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> flight.getDepartureTime().toLocalDate().isEqual(searchDate))
                .filter(flight -> flight.getAvailableSeats() >= numberOfPassengers)
                .collect(Collectors.toList());
    }
}
