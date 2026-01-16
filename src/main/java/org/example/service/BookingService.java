package org.example.service;

import org.example.entity.Addon;
import org.example.entity.Booking;
import org.example.entity.BookingStatus;
import org.example.entity.BookingType;
import org.example.entity.Car;
import org.example.repository.BookingRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingRepository repo = new BookingRepository();
    private final EmailService emailService = new EmailService();

    public boolean isCarAvailable(Car car, LocalDateTime start, LocalDateTime end) {
        return repo.isCarAvailable(car, start, end);
    }

    public double calculatePrice(Car car,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 BookingType type,
                                 List<Addon> addons) {

        double basePrice = 0;

        if (type == BookingType.HOURLY) {
            long hours = Duration.between(start, end).toHours();
            if (hours <= 0) hours = 1;
            basePrice = hours * car.getHourlyPrice();
        } else {
            long days = Duration.between(start, end).toDays();
            if (days <= 0) days = 1;
            basePrice = days * car.getDailyPrice();
        }

        double addonPrice = addons.stream()
            .mapToDouble(Addon::getPrice)
            .sum();

        return basePrice + addonPrice;
    }

    public void saveBooking(Booking booking) {
        repo.save(booking);
    }

    public List<Booking> getAllBookings() {
        return repo.findAll();
    }

    public String cancelBooking(Long bookingId) {
        return cancelBooking(bookingId, false);
    }

    public String cancelBooking(Long bookingId, boolean force) {
        Booking booking = repo.findById(bookingId);
        if (booking == null) {
            return "Bokningen hittades inte.";
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return "Bokningen är redan avbokad.";
        }

        if (!force) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(booking.getStartDate().minusHours(24))) {
                return "Bokningen kan endast avbokas senast 24 timmar innan upphämtning.";
            }
        }

        booking.setStatus(BookingStatus.CANCELLED);
        repo.save(booking);

        emailService.sendCancellationEmail(booking.getEmail(), booking.getFirstName());

        return "Bokningen har avbokats framgångsrikt.";
    }

    public LocalDateTime getNextAvailableTime(Car car) {
        List<Booking> futureBookings = repo.findFutureBookingsByCar(car);
        if (futureBookings.isEmpty()) {
            return LocalDateTime.now();
        }

        LocalDateTime nextAvailable = LocalDateTime.now();
        for (Booking b : futureBookings) {
            if (nextAvailable.isBefore(b.getStartDate())) {
                return nextAvailable;
            }
            nextAvailable = b.getEndDate();
        }
        return nextAvailable;
    }
}
