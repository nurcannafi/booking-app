package domain.dao.impl.database;

import domain.dao.FlightDao;
import domain.entity.FlightEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresFlightDaoImpl implements FlightDao {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flights";
    private static final String USER = "postgres";
    private static final String PASSWORD = "4277";
    private Connection connection;

    public PostgresFlightDaoImpl() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FlightEntity> findFlightsByDestination(String destination) {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights WHERE destination = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, destination);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                flights.add(new FlightEntity(
                        res.getTimestamp("date_time").toLocalDateTime(),
                        res.getString("departure_location"),
                        res.getString("destination"),
                        res.getInt("available_seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureLocation(String departureLocation) {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights WHERE departure_location = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, departureLocation);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                flights.add(new FlightEntity(
                        res.getTimestamp("date_time").toLocalDateTime(),
                        res.getString("departure_location"),
                        res.getString("destination"),
                        res.getInt("available_seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }


    @Override
    public List<FlightEntity> findAvailableFlights(int minimumSeats) {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights WHERE available_seats >= ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, minimumSeats);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                flights.add(new FlightEntity(
                        res.getTimestamp("date_time").toLocalDateTime(),
                        res.getString("departure_location"),
                        res.getString("destination"),
                        res.getInt("available_seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public boolean updateAvailableSeats(String flightId, int newAvailableSeats) {
        String query = "UPDATE flights SET available_seats = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newAvailableSeats);
            statement.setString(2, flightId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(FlightEntity entity) {
        String query = "INSERT INTO flights(date_time, departure_location, destination, available_seats) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setString(2, entity.getDepartureLocation());
            statement.setString(3, entity.getDestination());
            statement.setInt(4, entity.getAvailableSeats());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<FlightEntity> getById(String id) {
        String query = "SELECT * FROM flights WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                FlightEntity flight = new FlightEntity(
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getString("departure_location"),
                        rs.getString("destination"),
                        rs.getInt("available_seats")
                );
                return Optional.of(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                flights.add(new FlightEntity(
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getString("departure_location"),
                        rs.getString("destination"),
                        rs.getInt("available_seats")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public boolean update(FlightEntity entity) {
        String query = "UPDATE flights SET date_time = ?, departure_location = ?, destination = ?, available_seats = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setString(2, entity.getDepartureLocation());
            statement.setString(3, entity.getDestination());
            statement.setInt(4, entity.getAvailableSeats());
            statement.setString(5, entity.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM flights WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
