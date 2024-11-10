package domain.entity;

import java.time.LocalDateTime;
import java.util.List;

public class BookingEntity {

    private String id;
    private String flightId;
    private List<String> passengerNames;
    private LocalDateTime bookingTime;

    public BookingEntity(String id, String flightId, List<String> passengerNames) {
        this.id = id;
        this.flightId = flightId;
        this.passengerNames = passengerNames;
        this.bookingTime = bookingTime;
    }

    public BookingEntity(String flightId, List<String> passenger) {
        this.flightId = flightId;
        this.passengerNames = passenger;
        this.bookingTime = bookingTime;
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

    public List<String> getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(List<String> passengerNames) {
        this.passengerNames = passengerNames;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return String.format("BookingEntity{id='%s', flightId='%s', passengerNames='%s', bookingTime='%s'}", id, flightId, passengerNames, bookingTime);
    }
}
