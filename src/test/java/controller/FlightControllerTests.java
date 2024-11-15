package controller;

import domain.entity.FlightEntity;
import exception.FlightNotFoundException;
import exception.InvalidFlightOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FlightService;
import domain.dao.impl.InMemoryFlightDaoImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlightControllerTests {

    private FlightService flightService;
    private FlightController flightController;

    @BeforeEach
    public void setUp() {
        flightService = new FlightService(new InMemoryFlightDaoImpl());
        flightController = new FlightController(flightService);
    }

    @Test
    public void testAddFlight_Success() {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);

        String result = flightController.addFlight(flightEntity);
        assertEquals("Flight added successfully.", result);

        Optional<FlightEntity> addedFlight = flightService.getFlightById(flightEntity.getId());
        assertTrue(addedFlight.isPresent());
        assertEquals(flightEntity.getDepartureLocation(), addedFlight.get().getDepartureLocation());
    }

    @Test
    public void testAddFlight_Failure() {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 0);

        String result = flightController.addFlight(flightEntity);
        assertEquals("Failed to add flight.", result);
    }

    @Test
    public void testGetFlightById_Success() {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);
        flightService.addFlight(flightEntity);

        String result = flightController.getFlightById(flightEntity.getId());
        assertNotNull(result);
        assertTrue(result.contains("Kiev"));
        assertTrue(result.contains("London"));
    }

    @Test
    public void testGetFlightById_Failure() {
        String flightId = "non-existing-id";

        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class, () -> {
            flightController.getFlightById(flightId);
        });
        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    public void testUpdateFlight_Success() throws IllegalAccessException {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);
        flightService.addFlight(flightEntity);

        flightEntity.setDepartureLocation("Paris");

        String result = flightController.updateFlight(flightEntity);
        assertEquals("Flight updated successfully.", result);

        Optional<FlightEntity> updatedFlight = flightService.getFlightById(flightEntity.getId());
        assertTrue(updatedFlight.isPresent());
        assertEquals("Paris", updatedFlight.get().getDepartureLocation());
    }

    @Test
    public void testUpdateFlight_Failure() {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);

        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class, () -> {
            flightController.updateFlight(flightEntity);
        });
        assertEquals("Flight not found for update.", exception.getMessage());
    }

    @Test
    public void testDeleteFlight_Success() {
        FlightEntity flightEntity = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);
        flightService.addFlight(flightEntity);

        String result = flightController.deleteFlight(flightEntity.getId());
        assertEquals("Flight deleted successfully.", result);

        Optional<FlightEntity> deletedFlight = flightService.getFlightById(flightEntity.getId());
        assertFalse(deletedFlight.isPresent());
    }

    @Test
    public void testDeleteFlight_Failure() {
        String flightId = "non-existing-id";

        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class, () -> {
            flightController.deleteFlight(flightId);
        });
        assertEquals("Flight not found", exception.getMessage());
    }

    @Test
    public void testFindFlightsByDestination_Success() throws IllegalAccessException {
        FlightEntity flightEntity1 = new FlightEntity(
                LocalDateTime.now(), "Kiev", "London", 100);
        FlightEntity flightEntity2 = new FlightEntity(
                LocalDateTime.now(), "Kiev", "Paris", 100);
        flightService.addFlight(flightEntity1);
        flightService.addFlight(flightEntity2);

        List<FlightEntity> flights = flightController.findFlightsByDestination("London");
        assertEquals(1, flights.size());
        assertEquals("London", flights.get(0).getDestination());
    }

    @Test
    public void testFindFlightsByDestination_Failure() {
        InvalidFlightOperationException exception = assertThrows(InvalidFlightOperationException.class, () -> {
            flightController.findFlightsByDestination("");
        });
        assertEquals("Destination cannot be null or empty", exception.getMessage());
    }
}