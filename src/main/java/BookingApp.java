import domain.dao.BookingDao;
import domain.dao.FlightDao;
import domain.dao.impl.FileBookingDaoImpl;
import domain.dao.impl.FileFlightDaoImpl;
import domain.dao.impl.InMemoryBookingDaoImpl;
import domain.dao.impl.InMemoryFlightDaoImpl;
import model.dto.BookingDto;
import domain.entity.BookingEntity;
import domain.entity.FlightEntity;
import exception.BookingNotFoundException;
import exception.BookingNotFoundForPassengerNameException;
import exception.InvalidBookingException;
import exception.InvalidFlightOperationException;
import service.BookingService;
import service.FlightService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class BookingApp {

    public static void main(String[] args) {
        final FlightDao flightDao =
//                                  new InMemoryFlightDaoImpl();
//                                  new PostgresFlightDaoImpl();
                new FileFlightDaoImpl();

        final BookingDao bookingDao =
//                                    new InMemoryBookingDaoImpl();
//                                    new PostgresBookingDaoImpl();
                new FileBookingDaoImpl();

        FlightService flightService = new FlightService(flightDao);
        BookingService bookingService = new BookingService(bookingDao, flightService);

        flightDao.add(new FlightEntity(LocalDateTime.now().plusHours(1), "Kiev",
                "New York", 50));
        flightDao.add(new FlightEntity(LocalDateTime.now().plusHours(2), "Kiev",
                "Los Angeles", 75));
        flightDao.add(new FlightEntity(LocalDateTime.now().plusHours(3), "Kiev",
                "Tokyo", 60));
        flightDao.add(new FlightEntity(LocalDateTime.now().plusHours(4), "Kiev",
                "Sydney", 90));
        flightDao.add(new FlightEntity(LocalDateTime.now().plusHours(5), "Kiev",
                "Paris", 45));

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("\n----Booking Application----");
        while (!exit) {
            showMenu();

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> displayOnlineBoard(flightService);
                case 2 -> showFlightInfo(scanner, flightService);
                case 3 -> searchAndBookFlight(scanner, flightService, bookingService);
                case 4 -> cancelBooking(scanner, bookingService);
                case 5 -> viewMyFlights(scanner, bookingService);
                case 6 -> {
                    exit = true;
                    System.out.println("Exiting application...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println("1. Online-board (Flights from Kiev in the next 24 hours)");
        System.out.println("2. Show flight info by flight ID");
        System.out.println("3. Search and book a flight");
        System.out.println("4. Cancel a booking");
        System.out.println("5. My flights");
        System.out.println("6. Exit");
        System.out.println("Select an option : ");
    }

    private static void displayOnlineBoard(FlightService flightService) {
        try {
            System.out.println("\n---Online-board: Flights from Kiev in the next 24 hours---");
            List<FlightEntity> flightsFromKiev = flightService.getAllFlights();
            flightsFromKiev.stream()
                    .filter(flight -> flight.getDepartureLocation().equalsIgnoreCase("Kiev"))
                    .filter(flight -> flight.getDepartureTime().isAfter(LocalDateTime.now()))
                    .filter(flight -> flight.getDepartureTime().isBefore(LocalDateTime.now().plusHours(24)))
                    .forEach(System.out::println);
        } catch (InvalidFlightOperationException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void showFlightInfo(Scanner scanner, FlightService flightService) {
        try {
            System.out.println("\nEnter flight Id to view details : ");
            String flightId = scanner.nextLine();
            flightService.getFlightById(flightId).
                    orElseThrow(() -> new BookingNotFoundException(flightId));
        } catch (BookingNotFoundException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private static void searchAndBookFlight(Scanner scanner, FlightService flightService, BookingService bookingService) {
        try {
            System.out.println("\nEnter flight search criteria : ");
            System.out.println("Destination : ");
            String destination = scanner.nextLine();
            System.out.println("Date (YYYY-MM-DD) : ");
            String date = scanner.nextLine();
            System.out.println("Number of passengers : ");
            int numberOfPassengers = scanner.nextInt();
            scanner.nextLine();

            List<FlightEntity> availableFlights =
                    flightService.searchFlights(destination, date, numberOfPassengers);
            if (availableFlights.isEmpty()) {
                throw new InvalidFlightOperationException("No available flights found.");
            }

            System.out.println("\nAvailable Flights:");
            for (int i = 0; i < availableFlights.size(); i++) {
                System.out.println((i + 1) + ". " + availableFlights.get(i));
            }

            System.out.print("\nSelect flight number to book (or 0 to cancel): ");
            int selectedFlightIndex = scanner.nextInt() - 1;
            scanner.nextLine();

            if (selectedFlightIndex >= 0 && selectedFlightIndex < availableFlights.size()) {
                FlightEntity selectedFlight = availableFlights.get(selectedFlightIndex);
                System.out.print("\nEnter passenger names " +
                        "(comma separated, format: FirstName,LastName,Age): ");
                String passengersInput = scanner.nextLine();
                List<String> passengerNames = List.of(passengersInput.split(","));
                BookingDto bookingDto = new BookingDto("1", selectedFlight.getId(), passengerNames);
                boolean bookingAdded = bookingService.addBooking(bookingDto);
                if (bookingAdded) {
                    System.out.println("Booking successful!");
                } else {
                    throw new InvalidBookingException("Booking failed.");
                }
            }
        } catch (InvalidFlightOperationException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void cancelBooking(Scanner scanner, BookingService bookingService) {
        try {
            System.out.print("\nEnter booking ID to cancel: ");
            String bookingId = scanner.nextLine();
            boolean canceled = bookingService.cancelBooking(bookingId);
            if (canceled) {
                System.out.println("Booking canceled successfully.");
            } else {
                throw new BookingNotFoundException(bookingId);
            }
        } catch (BookingNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void viewMyFlights(Scanner scanner, BookingService bookingService) {
        try {
            System.out.print("\nEnter your full name: ");
            String passengerName = scanner.nextLine();
            List<BookingEntity> bookings = bookingService.findBookingsByPassengerName(passengerName);
            if (bookings.isEmpty()) {
                throw new BookingNotFoundForPassengerNameException(passengerName);
            }
            System.out.println("\nYour bookings:");
            bookings.forEach(System.out::println);
        } catch (BookingNotFoundForPassengerNameException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
