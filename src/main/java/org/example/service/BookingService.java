package org.example.service;

import org.example.model.Booking;
import org.example.model.BookingStatus;
import org.example.model.Extra;
import org.example.repository.BookingRepository;
import org.example.repository.CarRepository;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository = new BookingRepository();
    private final CarRepository carRepository = new CarRepository();

    public double calculateTotalPrice(Booking booking) {
        if (booking.getCar() == null) return 0.0;

        long days = booking.getDurationInDays();
        if (days <= 0) days = 1;

        double carTotal = booking.getCar().getDailyPrice() * days;
        double extrasTotal = booking.getExtras().stream()
                .mapToDouble(Extra::getPrice)
                .sum();

        return carTotal + extrasTotal;
    }

    public void createBooking(Booking booking) {
        if (booking.getCar() == null || !booking.getCar().isAvailable()) {
            throw new IllegalStateException("Bilen är inte tillgänglig för bokning.");
        }

        // Sätt status och spara
        booking.setStatus(BookingStatus.RESERVED);
        bookingRepository.save(booking);

        // Uppdatera bilens status så den inte kan bokas av någon annan
        booking.getCar().setAvailable(false);
        carRepository.save(booking.getCar());
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
