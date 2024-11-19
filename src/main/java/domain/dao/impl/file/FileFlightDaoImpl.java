package domain.dao.impl.file;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileFlightDaoImpl implements FlightDao {

    private static final String FILE_PATH = "src/main/resources/flights.txt";
    private final List<FlightEntity> flights;

    public FileFlightDaoImpl() {
        this.flights = loadFromFile();
    }

    private List<FlightEntity> loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<FlightEntity>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            objectOutputStream.writeObject(flights);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(FlightEntity entity) {
        boolean added = flights.add(entity);
        if (added) saveToFile();
        return added;
    }

    @Override
    public Optional<FlightEntity> getById(String id) {
        return flights.stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();
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
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        boolean removed = flights.removeIf(flight -> flight.getId().equals(id));
        if (removed) saveToFile();
        return removed;
    }

    @Override
    public List<FlightEntity> findFlightsByDestination(String destination) {
        return flights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .toList();
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        return flights.stream()
                .filter(flight -> flight.getDepartureLocation().equalsIgnoreCase(departureLocation))
                .toList();
    }

    @Override
    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        return flights.stream()
                .filter(flight -> flight.getAvailableSeats() >= minimumSeats)
                .toList();
    }

    @Override
    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        for (FlightEntity flight : flights) {
            if (flight.getId().equals(flightId)) {
                flight.setAvailableSeats(newAvailableSeats);
                saveToFile();
                return true;
            }
        }
        return false;
    }
}
