package domain.dao.impl;

import domain.dao.impl.memory.InMemoryFlightDaoImpl;
import domain.entity.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryFlightDaoImplTests {
    private InMemoryFlightDaoImpl flightDao;

    @BeforeEach
    void setUp() {
        flightDao = new InMemoryFlightDaoImpl();
    }

    @Test
    void testAddFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        assertTrue(flightDao.add(flight));
        assertEquals(1, flightDao.getAll().size());
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
    void testUpdateFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        flightDao.add(flight);

        String flightId = flight.getId();
        FlightEntity updatedFlight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 150);
        updatedFlight.setId(flightId);
        assertTrue(flightDao.update(updatedFlight));

        FlightEntity retrievedFlight = flightDao.getById(flightId).orElse(null);
        assertNotNull(retrievedFlight);
        assertEquals("Los Angeles", retrievedFlight.getDestination());
        assertEquals(150, retrievedFlight.getAvailableSeats());
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
    void testFindFlightsByDestination() {
        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15));
        flightDao.add(new FlightEntity(LocalDateTime.now(),
                "San Francisco", "Los Angeles", 15));
        List<FlightEntity> flights = flightDao.findFlightsByDestination("Los Angeles");
        assertEquals(2, flights.size());
    }

    @Test
    void testFindAvailableFlights() {
        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15));
        flightDao.add(new FlightEntity(LocalDateTime.now(),
                "San Francisco", "Los Angeles", 75));
        List<FlightEntity> flights = flightDao.findAvailableFlights(60);
        assertEquals(1, flights.size());
        assertEquals("San Francisco", flights.get(0).getDepartureLocation());
    }

    @Test
    void testUpdateAvailableSeats() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 15);
        flightDao.add(flight);
        String flightId = flight.getId();
        assertTrue(flightDao.updateAvailableSeats(flightId, 80));
        FlightEntity updatedFlight = flightDao.getById(flightId).orElse(null);
        assertNotNull(updatedFlight);
        assertEquals(80, updatedFlight.getAvailableSeats());
    }
}
