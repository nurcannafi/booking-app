package domain.dto;

public class FlightDto {
    private String id;
    private String destination;
    private int availableSeats;

    public FlightDto(String id, String destination, int availableSeats) {
        this.id = id;
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

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "FlightDto{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
