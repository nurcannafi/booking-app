//package domain.dao.impl;
//
//import domain.entity.FlightEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FileFlightDaoTests {
//
//    private static final String TEST_FILE_PATH = "src/test/resources/flights.dat";
//    private FileFlightDaoImpl flightDao;
//
//    @BeforeEach
//    void setUp() {
//        flightDao = new FileFlightDaoImpl();
//    }
//
//    @Test
//    void testAddFlight() {
//        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York", 100);
//        assertTrue(flightDao.add(flight));
//        assertEquals(1, flightDao.count());
//    }
//
//    @Test
//    void testGetById() {
//        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York", 100);
//        flightDao.add(flight);
//        FlightEntity retrievedFlight = flightDao.getById("1");
//        assertNotNull(retrievedFlight);
//        assertEquals("New York", retrievedFlight.getDestination());
//    }
//
//    @Test
//    void testUpdateFlight() {
//        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York", 100);
//        flightDao.add(flight);
//
//        FlightEntity updatedFlight = new FlightEntity(LocalDateTime.now(), "Los Angeles", 150);
//        assertTrue(flightDao.update(updatedFlight));
//
//        FlightEntity retrievedFlight = flightDao.getById("1");
//        assertEquals("Los Angeles", retrievedFlight.getDestination());
//    }
//
//    @Test
//    void testDeleteFlight() {
//        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York", 100);
//        flightDao.add(flight);
//        assertTrue(flightDao.delete("1"));
//        assertNull(flightDao.getById("1"));
//    }
//
//    @Test
//    void testCountFlights() {
//        assertEquals(0, flightDao.count());
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York", 100));
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "Los Angeles", 150));
//        assertEquals(2, flightDao.count());
//    }
//
//    @Test
//    void testFindFlightsByDestination() {
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York", 100));
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "Los Angeles", 150));
//        List<FlightEntity> flights = flightDao.findFlightsByDestination("New York");
//        assertEquals(1, flights.size());
//        assertEquals("New York", flights.get(0).getDestination());
//    }
//
//    @Test
//    void testFindAvailableFlights() {
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "New York", 100));
//        flightDao.add(new FlightEntity(LocalDateTime.now(), "Los Angeles", 50));
//        List<FlightEntity> flights = flightDao.findAvailableFlights(60);
//        assertEquals(1, flights.size());
//        assertEquals("New York", flights.get(0).getDestination());
//    }
//
//    @Test
//    void testUpdateAvailableSeats() {
//        FlightEntity flight = new FlightEntity(LocalDateTime.now(), "New York", 100);
//        flightDao.add(flight);
//        assertTrue(flightDao.updateAvailableSeats("1", 80));
//        assertEquals(80, flightDao.getById("1").getAvailableSeats());
//    }
//
//}
