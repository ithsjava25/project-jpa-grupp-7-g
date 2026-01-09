package org.example.service;

import org.example.entity.Booking;
import org.example.repository.BookingRepository;
import org.example.config.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.example.entity.Addon;
import org.example.entity.BookingType;
import org.example.entity.Car;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class BookingService {

    private final BookingRepository repo = new BookingRepository();

    public List<Booking> getAllBookings() {
        return repo.findAll();
    }

    public List<Booking> getBookingsForCar(Car car) {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery("SELECT b FROM Booking b WHERE b.car = :car ORDER BY b.startTime", Booking.class)
                .setParameter("car", car)
                .getResultList();
    }

    public boolean isCarAvailable(Car car, LocalDateTime start, LocalDateTime end) {
        List<Booking> bookings = getBookingsForCar(car);
        for (Booking b : bookings) {
            if (start.isBefore(b.getEndTime()) && end.isAfter(b.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    public LocalDateTime getNextAvailableTime(Car car) {
        List<Booking> bookings = getBookingsForCar(car);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime latestEnd = now;

        for (Booking b : bookings) {
            if (b.getEndTime().isAfter(latestEnd)) {
                if (b.getStartTime().isAfter(latestEnd)) {
                    // There is a gap
                    return latestEnd;
                }
                latestEnd = b.getEndTime();
            }
        }
        return latestEnd;
    }

    public double calculatePrice(Car car, LocalDateTime start, LocalDateTime end, BookingType type, List<Addon> addons) {
        Duration duration = Duration.between(start, end);
        double basePrice = 0;
        if (type == BookingType.HOURLY) {
            basePrice = Math.ceil(duration.toMinutes() / 60.0) * car.getPricePerHour();
        } else {
            basePrice = Math.ceil(duration.toHours() / 24.0) * car.getPricePerDay();
        }

        double addonPrice = addons.stream().mapToDouble(Addon::getPrice).sum();
        return basePrice + addonPrice;
    }

    public void saveBooking(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (booking.getId() == null) {
                em.persist(booking);
            } else {
                em.merge(booking);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
