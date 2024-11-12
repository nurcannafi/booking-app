package domain.dao.impl;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresBookingDaoImpl implements BookingDao {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bookings";
    private static final String USER = "postgres";
    private static final String PASSWORD = "4277";
    private Connection connection;

    public PostgresBookingDaoImpl() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(BookingEntity booking) {
        String query = "INSERT INTO bookings(id, flight_id, passenger_names) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, booking.getId());
            statement.setString(2, booking.getFlightId());
            statement.setString(3, String.join(",", booking.getPassengerNames()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BookingEntity getById(String id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                List<String> passengerNames = List.of(resultSet.getString("passenger_names").split(","));
                return new BookingEntity(
                        resultSet.getString("id"),
                        resultSet.getString("flight_id"),
                        passengerNames
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                List<String> passengerNames = List.of(resultSet.getString("passenger_names").split(","));
                bookings.add(new BookingEntity(
                        resultSet.getString("id"),
                        resultSet.getString("flight_id"),
                        passengerNames
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean update(BookingEntity booking) {
        String query = "UPDATE bookings SET flight_id = ?, passenger_names = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, booking.getFlightId());
            statement.setString(2, String.join(",", booking.getPassengerNames()));
            statement.setString(3, booking.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM bookings WHERE id = ?";
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
        String query = "SELECT COUNT(*) FROM bookings";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<BookingEntity> findBookingsByFlightId(String flightId) {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE flight_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, flightId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                List<String> passengerNames = List.of(resultSet.getString("passenger_names").split(","));
                bookings.add(new BookingEntity(
                        resultSet.getString("id"),
                        resultSet.getString("flight_id"),
                        passengerNames
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE passenger_names LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + passengerName + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                List<String> passengerNames = List.of(rs.getString("passenger_names").split(","));
                bookings.add(new BookingEntity(
                        rs.getString("id"),
                        rs.getString("flight_id"),
                        passengerNames
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        return delete(bookingId);
    }
}