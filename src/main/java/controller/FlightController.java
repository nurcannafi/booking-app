package controller;

import domain.entity.FlightEntity;
import service.FlightService;

import java.util.List;
import java.util.Optional;

public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    public String addFlight(FlightEntity flightEntity) {
        boolean isAdded = flightService.addFlight(flightEntity);
        return isAdded ? "Flight added successfully." : "Failed to add flight.";
    }

    public String getFlightById(String id) {
        Optional<FlightEntity> flight = flightService.getFlightById(id);
        return flight.map(Object::toString).orElse("Flight not found.");
    }

    public List<FlightEntity> getAllFlights() {
        return flightService.getAllFlights();
    }

    public List<FlightEntity> findFlightsByDestination(String destination) throws IllegalAccessException {
        return flightService.findFlightsByDestination(destination);
    }

    public List<FlightEntity> findAvailableFlights(int minimumSeats) throws IllegalAccessException {
        return flightService.findAvailableFlights(minimumSeats);
    }

    public String updateFlight(FlightEntity flight) throws IllegalAccessException {
        boolean isUpdated = flightService.updateFlight(flight);
        return isUpdated ? "Flight updated successfully." : "Flight not found.";
    }

    public String deleteFlight(String id) {
        boolean isDeleted = flightService.deleteFlight(id);
        return isDeleted ? "Flight deleted successfully." : "Flight not found.";
    }

    public String updateAvailableSeats(String flightId, int newAvailableSeats) {
        boolean isUpdated = flightService.updateAvailableSeats(flightId, newAvailableSeats);
        return isUpdated ? "Available seats updated successfully." : "Flight not found.";
    }

    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        return flightService.findFlightsByDepartureLocation(departureLocation);
    }
}
