package domain.dao.impl.file;

import domain.dao.BookingDao;
import domain.entity.BookingEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileBookingDaoImpl implements BookingDao {

    private static final String FILE_PATH = "src/main/resources/bookings.txt";
    private final List<BookingEntity> bookings;

    public FileBookingDaoImpl() {
        this.bookings = loadFromFile();
    }

    private List<BookingEntity> loadFromFile() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<BookingEntity>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            objectOutputStream.writeObject(bookings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean add(BookingEntity entity) {
        boolean added = bookings.add(entity);
        if (added) saveToFile();
        return added;
    }

    @Override
    public Optional<BookingEntity> getById(String id) {
        return bookings.stream()
                .filter(booking -> booking.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<BookingEntity> getAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public boolean update(BookingEntity entity) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(entity.getId())) {
                bookings.set(i, entity);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        boolean removed = bookings.removeIf(booking -> booking.getId().equals(id));
        if (removed) saveToFile();
        return removed;
    }

    @Override
    public List<BookingEntity> findBookingsByPassengerName(String passengerName) {
        return bookings.stream()
                .filter(booking -> booking.getPassengers().stream()
                        .anyMatch(passenger -> passenger.getFirstName().equalsIgnoreCase(passengerName) ||
                                passenger.getLastName().equalsIgnoreCase(passengerName)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelBooking(String bookingId) {
        return delete(bookingId);
    }
}
