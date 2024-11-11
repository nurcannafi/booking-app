package domain;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileFlightDao implements FlightDao {

    private static final String FILE_PATH = "src/main/resources/flights.txt";

    @Override
    public List<FlightEntity> findFlightsByDestination(String destination) {
        return getAll().stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .toList();
    }

    @Override
    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        return getAll().stream()
                .filter(flight -> flight.getAvailableSeats() >= minimumSeats)
                .toList();
    }

    @Override
    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        List<FlightEntity> flights = getAll();
        boolean updated = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (FlightEntity flight : flights) {
                if (flight.getId().equals(flightId)) {
                    flight.setAvailableSeats(newAvailableSeats);
                    updated = true;
                }
                writer.write(flight.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean add(FlightEntity entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(entity.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FlightEntity getById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                FlightEntity flight = parseFlight(line);
                if (flight.getId().equals(id)) {
                    return flight;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                flights.add(parseFlight(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public boolean update(FlightEntity entity) {
        List<FlightEntity> flights = getAll();
        boolean updated = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (FlightEntity flight : flights) {
                if (flight.getId().equals(entity.getId())) {
                    writer.write(entity.toString());
                    updated = true;
                } else {
                    writer.write(flight.toString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean delete(String id) {
        List<FlightEntity> flights = getAll();
        boolean deleted = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (FlightEntity flight : flights) {
                if (!flight.getId().equals(id)) {
                    writer.write(flight.toString());
                    writer.newLine();
                } else {
                    deleted = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public long count() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return reader.lines().count();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    protected FlightEntity parseFlight(String line) {
        String[] parts = line.split(", ");

        String id = parts[0].split("=")[1].replace("'", "").replace("FlightEntity{id='", "");
        String dateTimeString = parts[1].split("=")[1];
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
        String destination = parts[2].split("=")[1].replace("'", "");
        int availableSeats = Integer.parseInt(parts[3].split("=")[1].replace("}", ""));

        return new FlightEntity(id, dateTime, destination, availableSeats);
    }
}
