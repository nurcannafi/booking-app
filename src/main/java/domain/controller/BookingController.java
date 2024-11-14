package domain.controller;

import domain.dto.BookingDto;
import domain.service.BookingService;
import domain.entity.BookingEntity;
import domain.entity.PassengerEntity;

import java.util.List;
import java.util.Optional;

public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public String addBooking(BookingDto bookingDto) {
        boolean isAdded = bookingService.addBooking(bookingDto);
        return isAdded ? "Booking added successfully." : "Failed to add booking.";
    }

    public String getBookingById(String id) {
        Optional<BookingDto> booking = bookingService.getBookingById(id).map(this::convertToDto);
        return booking.map(Object::toString).orElse("Booking not found.");
    }

    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings().stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<BookingDto> findBookingsByPassengerName(String passengerName) {
        return bookingService.findBookingsByPassengerName(passengerName).stream()
                .map(this::convertToDto)
                .toList();
    }

    public String updateBooking(BookingDto bookingDto) {
        boolean isUpdated = bookingService.updateBooking(bookingDto);
        return isUpdated ? "Booking updated successfully." : "Booking not found.";
    }

    public String cancelBooking(String bookingId) {
        boolean isCancelled = bookingService.cancelBooking(bookingId);
        return isCancelled ? "Booking canceled successfully." : "Booking not found.";
    }

    private BookingDto convertToDto(BookingEntity bookingEntity) {
        // Assuming BookingDto has a constructor that accepts BookingEntity
        return new BookingDto(bookingEntity.getId(), bookingEntity.getFlightId(),
                bookingEntity.getPassengers().stream().map(PassengerEntity::toString).toList());
    }
}
