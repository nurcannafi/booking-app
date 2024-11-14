package domain.dao.impl;

import domain.entity.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileFlightDaoImplTests {

    private static final String TEST_FILE_PATH = "src/test/resources/flights.dat";
    private FileFlightDaoImpl flightDao;

    @BeforeEach
    void setUp() {
        flightDao = new FileFlightDaoImpl();
    }

    @Test
    void testAddFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100);
        assertTrue(flightDao.add(flight));

        List<FlightEntity> flights = flightDao.getAll();
        assertEquals(1, flights.size());
    }

    @Test
    void testGetById() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100);
        flightDao.add(flight);

        Optional<FlightEntity> retrievedFlightOptional = flightDao.getById(flight.getId());
        assertTrue(retrievedFlightOptional.isPresent());
        FlightEntity retrievedFlight = retrievedFlightOptional.get();

        assertEquals("New York", retrievedFlight.getDepartureLocation());
        assertEquals("Los Angeles", retrievedFlight.getDestination());
    }

    @Test
    void testUpdateFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100);
        flightDao.add(flight);

        FlightEntity updatedFlight = new FlightEntity(flight.getDepartureTime(), "New York",
                "Chicago", 150);
        updatedFlight.setId(flight.getId());

        assertTrue(flightDao.update(updatedFlight));

        Optional<FlightEntity> retrievedFlightOptional = flightDao.getById(flight.getId());
        assertTrue(retrievedFlightOptional.isPresent());
        FlightEntity retrievedFlight = retrievedFlightOptional.get();
        assertEquals("Chicago", retrievedFlight.getDestination());
        assertEquals(150, retrievedFlight.getAvailableSeats());
    }

    @Test
    void testDeleteFlight() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(),
                "New York", "Los Angeles", 100);
        flightDao.add(flight);

        assertTrue(flightDao.delete(flight.getId()));
        Optional<FlightEntity> deletedFlightOptional = flightDao.getById(flight.getId());
        assertFalse(deletedFlightOptional.isPresent());
    }

    @Test
    void testFindFlightsByDestination() {
        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100));
        flightDao.add(new FlightEntity(LocalDateTime.now(),
                "San Francisco", "Los Angeles", 150));

        List<FlightEntity> flights = flightDao.findFlightsByDestination("Los Angeles");
        assertEquals(2, flights.size());

        assertTrue(flights.stream().allMatch(flight -> "Los Angeles".equals(flight.getDestination())));
    }

    @Test
    void testFindAvailableFlights() {
        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100));
        flightDao.add(new FlightEntity(LocalDateTime.now(),
                "San Francisco", "Los Angeles", 50));

        List<FlightEntity> flights = flightDao.findAvailableFlights(50);
        assertEquals(1, flights.size());
        assertEquals("New York", flights.get(0).getDepartureLocation());
    }

    @Test
    void testUpdateAvailableSeats() {
        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York",
                "Los Angeles", 100);
        flightDao.add(flight);

        assertTrue(flightDao.updateAvailableSeats(flight.getId(), 80));

        Optional<FlightEntity> updatedFlightOptional = flightDao.getById(flight.getId());
        assertTrue(updatedFlightOptional.isPresent());
        assertEquals(80, updatedFlightOptional.get().getAvailableSeats());
    }

}
