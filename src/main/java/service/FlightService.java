package service;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.util.List;
import java.util.Optional;

public class FlightService {

    private final FlightDao flightDao;

    public FlightService(FlightDao flightDao){
        this.flightDao = flightDao;
    }

    public boolean addFlight(FlightEntity flightEntity){
        if(flightEntity.getAvailableSeats() > 0 && flightEntity.getDestination() != null && flightEntity.getDepartureLocation() != null){
            return flightDao.add(flightEntity);
        }
        return false;
    }

    public Optional<FlightEntity> getFlightById(String id){
        return flightDao.getById(id);
    }

    public List<FlightEntity> getAllFlights(){
        return flightDao.getAll();
    }

    public List<FlightEntity> findFlightsByDestination(String destination) throws IllegalAccessException {
        if(destination == null || destination.isEmpty()){
            throw new IllegalAccessException("Destination cannot be null or empty");
        }
        return flightDao.findFlightsByDestination(destination);
    }

    public List<FlightEntity> findAvailableFlights(int minimumSeats) throws IllegalAccessException {
        if(minimumSeats < 1){
            throw new IllegalAccessException("Minimum seats must be at least 1");
        }
        return flightDao.findAvailableFlights(minimumSeats);
    }

    public boolean updateFlight(FlightEntity flight) throws IllegalAccessException {
        if(flight.getId() == null || flight.getId().isEmpty()){
            throw new IllegalAccessException("Flight ID cannot be null or empty");
        }
        return flightDao.update(flight);
    }

    public boolean deleteFlight(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Flight ID cannot be null or empty");
        }
        return flightDao.delete(id);
    }

    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        if (newAvailableSeats < 0) {
            throw new IllegalArgumentException("Available seats cannot be negative");
        }
        return flightDao.updateAvailableSeats(flightId, newAvailableSeats);
    }

    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        if (departureLocation == null || departureLocation.isEmpty()) {
            throw new IllegalArgumentException("Departure location cannot be null or empty");
        }
        return flightDao.findFlightsByDepartureLocation(departureLocation);
    }
}
