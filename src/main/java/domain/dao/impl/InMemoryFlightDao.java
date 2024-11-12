package domain.dao.impl;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.util.List;
import java.util.ArrayList;

public class InMemoryFlightDao implements FlightDao {

    private final List<FlightEntity> flights = new ArrayList<>();

    @Override
    public List<FlightEntity> findFlightsByDestination(String destination) {
        return flights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .toList();
    }

    @Override
    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        return flights.stream()
                .filter(flight -> flight.getAvailableSeats() >= minimumSeats)
                .toList();
    }

    @Override
    public boolean updateAvailableSeats(String fligthId, int newAvailableSeats) {
        FlightEntity flight = getById(fligthId);
        if (flight != null) {
            flight.setAvailableSeats(newAvailableSeats);
            return true;
        }
        return false;
    }

    @Override
    public boolean add(FlightEntity entity) {
        return flights.add(entity);
    }

    @Override
    public FlightEntity getById(String id) {
        return flights.stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<FlightEntity> getAll() {
        return new ArrayList<>(flights);
    }

    @Override
    public boolean update(FlightEntity entity) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getId().equals(entity.getId())) {
                flights.set(i, entity);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return flights.removeIf(flight -> flight.getId().equals(id));
    }

    @Override
    public long count() {
        return flights.size();
    }
}
