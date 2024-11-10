package domain.entity;

import java.time.LocalDateTime;

public class FlightEntity {

    private String id;
    private LocalDateTime dateTime;
    private String destination;
    private int availableSeats;

    public FlightEntity(LocalDateTime dateTime, String destination, int availableSeats) {
        this.dateTime = dateTime;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public FlightEntity(String id, LocalDateTime dateTime, String destination, int availableSeats) {
        this.id = id;
        this.dateTime = dateTime;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return String.format("FlightEntity{id='%s', dateTime=%s, destination='%s', availableSeats=%d}", id, dateTime,
                destination, availableSeats);
    }
}
