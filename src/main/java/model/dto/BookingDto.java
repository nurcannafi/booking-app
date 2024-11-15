package model.dto;

import java.util.List;

public class BookingDto {
    private String id;
    private String flightId;
    private List<String> passengerNames;

    public BookingDto(String id, String flightId, List<String> passengerNames) {
        this.id = id;
        this.flightId = flightId;
        this.passengerNames = passengerNames;
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

    @Override
    public String toString() {
        return "BookingDto{" +
                "id='" + id + '\'' +
                ", flightId='" + flightId + '\'' +
                ", passengerNames=" + passengerNames +
                '}';
    }
}
