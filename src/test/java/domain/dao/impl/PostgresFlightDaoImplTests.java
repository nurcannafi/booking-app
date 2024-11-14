package domain.dao.impl;

import domain.entity.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgresFlightDaoImplTests {
    private PostgresFlightDaoImpl flightDao;

    @BeforeEach
    void setUp() {
        flightDao = new PostgresFlightDaoImpl();
    }

    @Test
    void testAddFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        assertTrue(flightDao.add(flight));
        List<FlightEntity> flights = flightDao.getAll();
        assertTrue(flights.size() > 0);
    }

    @Test
    void testFindFlightsByDestination() {
        FlightEntity flight1 = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        FlightEntity flight2 = new FlightEntity(LocalDateTime.now(),
                "San Francisco", "Los Angeles", 10);
        flightDao.add(flight1);
        flightDao.add(flight2);
        List<FlightEntity> flights = flightDao.findFlightsByDestination("Los Angeles");
        assertEquals(2, flights.size());
    }

    @Test
    void testFindFlightsByDepartureLocation() {
        FlightEntity flight1 = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        FlightEntity flight2 = new FlightEntity(LocalDateTime.now(), "New York",
                "San Francisco", 10);
        flightDao.add(flight1);
        flightDao.add(flight2);
        List<FlightEntity> flights = flightDao.findFlightsByDepartureLocation("New York");
        assertEquals(2, flights.size());
    }

    @Test
    void testFindAvailableFlights() {
        FlightEntity flight1 = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 5);
        FlightEntity flight2 = new FlightEntity(LocalDateTime.now(), "New York",
                "San Francisco", 50);
        flightDao.add(flight1);
        flightDao.add(flight2);
        List<FlightEntity> flights = flightDao.findAvailableFlights(20);
        assertEquals(1, flights.size());
    }

    @Test
    void testUpdateAvailableSeats() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        flightDao.add(flight);
        String flightId = flight.getId();
        assertTrue(flightDao.updateAvailableSeats(flightId, 100));
        FlightEntity updatedFlight = flightDao.getById(flightId).orElse(null);
        assertNotNull(updatedFlight);
        assertEquals(100, updatedFlight.getAvailableSeats());
    }

    @Test
    void testGetById() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        flightDao.add(flight);
        String flightId = flight.getId();
        FlightEntity retrievedFlight = flightDao.getById(flightId).orElse(null);
        assertNotNull(retrievedFlight);
        assertEquals("Los Angeles", retrievedFlight.getDestination());
    }

    @Test
    void testDeleteFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        flightDao.add(flight);
        String flightId = flight.getId();
        assertTrue(flightDao.delete(flightId));
        assertFalse(flightDao.getById(flightId).isPresent());
    }

    @Test
    void testGetAllFlights() {
        FlightEntity flight1 = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        FlightEntity flight2 = new FlightEntity(LocalDateTime.now(), "San Francisco",
                "Los Angeles", 10);
        flightDao.add(flight1);
        flightDao.add(flight2);

        List<FlightEntity> flights = flightDao.getAll();
        assertEquals(2, flights.size());
    }
}
