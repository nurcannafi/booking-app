package domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class BookingEntity implements Serializable {

    private String id;
    private String flightId;
    private List<PassengerEntity> passengers;
    private final LocalDateTime bookingTime;

    public BookingEntity(String id, String flightId, List<PassengerEntity> passengers) {
        this.id = id;
        this.flightId = flightId;
        this.passengers = passengers;
        this.bookingTime = LocalDateTime.now();
    }

    public BookingEntity(String flightId, List<PassengerEntity> passengers) {
        this.flightId = flightId;
        this.passengers = passengers;
        this.bookingTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public List<PassengerEntity> getPassengers() {
        return passengers;
    }

    public void setPassengerNames(List<PassengerEntity> passengers) {
        this.passengers = passengers;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return String.format("BookingEntity{id='%s', flightId='%s', passengerNames='%s', bookingTime='%s'}", id,
                flightId, passengers, bookingTime);
    }
}
