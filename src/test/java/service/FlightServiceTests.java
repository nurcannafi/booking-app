package service;

import domain.dao.FlightDao;
import domain.dao.impl.InMemoryFlightDaoImpl;
import domain.entity.FlightEntity;
import exception.InvalidFlightOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FlightService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FlightServiceTests {

    private FlightService flightService;
    private FlightDao flightDao;

    @BeforeEach
    void setUp() {
        flightDao = new InMemoryFlightDaoImpl();
        flightService = new FlightService(flightDao);
    }

    @Test
    void addFlightTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "Istanbul", 100);
        assertTrue(flightService.addFlight(flight));

        FlightEntity invalidFlight = new FlightEntity(
                LocalDateTime.now().plusDays(1), null, "Istanbul", 100);
        assertFalse(flightService.addFlight(invalidFlight));

    }

    @Test
    void getFlightByIdTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "Istanbul", 100);
        flightDao.add(flight);
        Optional<FlightEntity> result = flightService.getFlightById(flight.getId());
        assertTrue(result.isPresent());
        assertEquals(flight.getId(), result.get().getId());

        Optional<FlightEntity> result1 = flightService.getFlightById("non-existent-id");
        assertTrue(result1.isEmpty());
    }

    @Test
    void getAllFlightsTest() {
        FlightEntity flight1 = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "Istanbul", 100);
        FlightEntity flight2 = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "London", 50);
        flightDao.add(flight1);
        flightDao.add(flight2);
        List<FlightEntity> allFlights = flightService.getAllFlights();
        assertEquals(2, allFlights.size());
    }

    @Test
    void findFlightByDestinationTest(){
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Baku", "Istanbul", 100);
        flightDao.add(flight);
        List<FlightEntity> result = flightService.findFlightsByDestination("Istanbul");
        assertEquals(1, result.size());
        assertEquals("Istanbul", result.get(0).getDestination());
    }

    @Test
    void updateFlightTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "London", 100);
        flightDao.add(flight);
        flight.setAvailableSeats(90);
        assertTrue(flightService.updateFlight(flight));
        assertEquals(90, flightDao.getById(flight.getId()).get().getAvailableSeats());
    }

    @Test
    void deleteFlightTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "Paris", 100);
        flightDao.add(flight);
        assertTrue(flightService.deleteFlight(flight.getId()));
        assertTrue(flightDao.getById(flight.getId()).isEmpty());

        assertThrows(InvalidFlightOperationException.class, () -> flightService.deleteFlight(null));
    }

    @Test
    void updateAvailableSeatsTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1), "Kiev", "Baku", 100);
        flightDao.add(flight);
        assertTrue(flightService.updateAvailableSeats(flight.getId(), 90));
        assertEquals(90, flightDao.getById(flight.getId()).get().getAvailableSeats());
    }

    @Test
    void searchFlightsTest() {
        FlightEntity flight = new FlightEntity(
                LocalDateTime.now().plusDays(1).withHour(10), "Kiev", "London", 100);
        flightDao.add(flight);
        List<FlightEntity> result = flightService.searchFlights(
                "London", LocalDate.now().plusDays(1).toString(), 50);
        assertEquals(1, result.size());
        assertEquals("London", result.get(0).getDestination());
    }

}
