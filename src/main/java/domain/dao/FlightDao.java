package domain.dao;

import domain.entity.FlightEntity;

import java.util.List;

public interface FlightDao extends Dao<FlightEntity> {

    List<FlightEntity> findFlightsByDestination(String destination);

    List<FlightEntity> findFlightsByDepartureLocation(String departureLocation);

    List<FlightEntity> findAvailableFlights(int minimumSeats);

    boolean updateAvailableSeats(String fligthId, int newAvailableSeats);
}
