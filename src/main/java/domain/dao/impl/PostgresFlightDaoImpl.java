package domain.dao.impl;

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
                        res.getString("id"),
                        res.getTimestamp("date_time").toLocalDateTime(),
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
                        res.getString("id"),
                        res.getTimestamp("date_time").toLocalDateTime(),
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
    public boolean updateAvailableSeats(String fligthId, int newAvailableSeats) {
        String query = "UPDATE flights SET available_seats = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newAvailableSeats);
            statement.setString(2, fligthId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(FlightEntity entity) {
        String query = "INSERT INTO flights(date_time, destination, available_seats) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime()));
            statement.setString(2, entity.getDestination());
            statement.setInt(3, entity.getAvailableSeats());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public FlightEntity getById(String id) {
        String query = "SELECT *FROM flights WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new FlightEntity(
                        rs.getString("id"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        rs.getString("destination"),
                        rs.getInt("available_seats")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                flights.add(new FlightEntity(
                        rs.getString("id"),
                        rs.getTimestamp("date_time").toLocalDateTime(),
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
        String query = "UPDATE flights SET date_time = ?, destination = ?, available_seats = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDateTime()));
            statement.setString(2, entity.getDestination());
            statement.setInt(3, entity.getAvailableSeats());
            statement.setString(4, entity.getId());

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

    @Override
    public long count() {
        String query = "SELECT COUNT(*) FROM flights";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return res.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
