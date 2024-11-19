package domain.dao.impl.memory;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryFlightDaoImpl implements FlightDao {

    private final Map<String, FlightEntity> flights = new HashMap<>();

    @Override
    public List<FlightEntity> findFlightsByDestination(String destination) {
        return flights.values().stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        return flights.values().stream()
                .filter(flight -> flight.getDepartureLocation().equalsIgnoreCase(departureLocation))
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        return flights.values().stream()
                .filter(flight -> flight.getAvailableSeats() >= minimumSeats)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        FlightEntity flight = flights.get(flightId);
        if (flight != null) {
            flight.setAvailableSeats(newAvailableSeats);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(FlightEntity entity) {
        flights.put(entity.getId(), entity);
        return true;
    }

    @Override
    public Optional<FlightEntity> getById(String id) {
        return Optional.ofNullable(flights.get(id));
    }

    @Override
    public List<FlightEntity> getAll() {
        return flights.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean update(FlightEntity entity) {
        if (flights.containsKey(entity.getId())) {
            flights.put(entity.getId(), entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return flights.remove(id) != null;
    }
}
