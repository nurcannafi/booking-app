package domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class FlightEntity implements Serializable {

    private String id;
    private LocalDateTime departureTime;
    private String departureLocation;
    private String destination;
    private int availableSeats;

    public FlightEntity(LocalDateTime departureTime, String departureLocation, String destination, int availableSeats) {
        this.id = UUID.randomUUID().toString();
        this.departureTime = departureTime;
        this.departureLocation = departureLocation;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
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
        return String.format("FlightEntity{id='%s', departureTime=%s, departureLocation='%s', destination='%s'" +
                        ", availableSeats=%d}",
                id, departureTime, departureLocation, destination, availableSeats);
    }
}
