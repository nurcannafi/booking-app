package domain.dto;

import java.time.LocalDateTime;

public class FlightDto {

    private String id;
    private String destination;
    private String departureLocation;
    private LocalDateTime departureTime;
    private int availableSeats;

    public FlightDto(String id, String destination, String departureLocation, LocalDateTime departureTime
            , int availableSeats) {
        this.id = id;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return String.format("FlightDto{id='%s', departureLocation='%s', departureTime=%s, destination='%s'" +
                        ", availableSeats=%d}",
                id, departureLocation, departureTime, destination, availableSeats);
    }
}
