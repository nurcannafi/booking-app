package controller;

import domain.entity.FlightEntity;
import exception.FlightNotFoundException;
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
        if (flight.isEmpty()) {
            throw new FlightNotFoundException("Flight not found");
        }
        return flight.get().toString();
    }

    public List<FlightEntity> getAllFlightsIn24hours() {
        return flightService.getAllFlightsIn24hours();
    }

    public List<FlightEntity> findFlightsByDestination(String destination) throws IllegalAccessException {
        return flightService.findFlightsByDestination(destination);
    }

    public List<FlightEntity> findAvailableFlights(int minimumSeats) throws IllegalAccessException {
        return flightService.findAvailableFlights(minimumSeats);
    }

    public String updateFlight(FlightEntity flight) throws IllegalAccessException {
        boolean isUpdated = flightService.updateFlight(flight);
        if (!isUpdated) {
            throw new FlightNotFoundException("Flight not found for update.");
        }
        return "Flight updated successfully.";
    }

    public String deleteFlight(String id) {
        boolean isDeleted = flightService.deleteFlight(id);
        if (!isDeleted) {
            throw new FlightNotFoundException("Flight not found");
        }
        return "Flight deleted successfully.";
    }

    public String updateAvailableSeats(String flightId, int newAvailableSeats) {
        boolean isUpdated = flightService.updateAvailableSeats(flightId, newAvailableSeats);
        if (!isUpdated) {
            throw new FlightNotFoundException("Flight not found");
        }
        return "Available seats updated successfully.";
    }

    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        return flightService.findFlightsByDepartureLocation(departureLocation);
    }
}
