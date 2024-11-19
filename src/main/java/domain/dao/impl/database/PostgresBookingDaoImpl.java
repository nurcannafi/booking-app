package domain.dao.impl.database;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            statement.setString(3, String.join(",", booking.getPassengers().stream()
                    .map(passenger -> passenger.getFirstName() + " " + passenger.getLastName())
                    .collect(Collectors.toList())));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<BookingEntity> getById(String id) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String passengerNamesString = resultSet.getString("passenger_names");
                List<PassengerEntity> passengerEntities = Arrays.stream(passengerNamesString.split(","))
                        .map(name -> new PassengerEntity(name.split(" ")[0], name.split(" ")[1], -1))
                        .collect(Collectors.toList());
                BookingEntity booking = new BookingEntity(
                        resultSet.getString("id"),
                        resultSet.getString("flight_id"),
                        passengerEntities
                );
                return Optional.of(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String passengerNamesString = resultSet.getString("passenger_names");
                List<PassengerEntity> passengerEntities = Arrays.stream(passengerNamesString.split(","))
                        .map(name -> new PassengerEntity(name.split(" ")[0], name.split(" ")[1], -1))
                        .collect(Collectors.toList());
                bookings.add(new BookingEntity(
                        resultSet.getString("id"),
                        resultSet.getString("flight_id"),
                        passengerEntities
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
            String passengersNames = booking.getPassengers().stream()
                    .map(p -> p.getFirstName() + " " + p.getLastName())
                    .collect(Collectors.joining(","));
            statement.setString(2, String.join(",", passengersNames));
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
    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        List<BookingEntity> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE passenger_names LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + passengerName + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String passengerNamesString = rs.getString("passenger_names");
                List<PassengerEntity> passengerEntities = Arrays.stream(passengerNamesString.split(","))
                        .map(name -> new PassengerEntity(name.split(" ")[0], name.split(" ")[1], -1))
                        .collect(Collectors.toList());
                bookings.add(new BookingEntity(
                        rs.getString("id"),
                        rs.getString("flight_id"),
                        passengerEntities
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
