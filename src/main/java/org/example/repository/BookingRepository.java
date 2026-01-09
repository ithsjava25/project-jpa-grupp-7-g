package org.example.repository;

import org.example.config.JpaUtil;
import org.example.entity.Booking;
import jakarta.persistence.EntityManager;
import java.util.List;

public class BookingRepository {

    public List<Booking> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
    }

    public void save(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(booking);
        em.getTransaction().commit();
        em.close();
    }

    public boolean isCarAvailable(org.example.entity.Car car, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        EntityManager em = JpaUtil.getEntityManager();
        Long count = em.createQuery(
                "SELECT COUNT(b) FROM Booking b " +
                "WHERE b.car = :car " +
                "AND ((b.startDate < :end AND b.endDate > :start))", Long.class)
            .setParameter("car", car)
            .setParameter("start", start)
            .setParameter("end", end)
            .getSingleResult();
        em.close();
        return count == 0;
    }

    public List<Booking> findFutureBookingsByCar(org.example.entity.Car car) {
        EntityManager em = JpaUtil.getEntityManager();
        List<Booking> bookings = em.createQuery(
                "SELECT b FROM Booking b " +
                "WHERE b.car = :car AND b.endDate > :now " +
                "ORDER BY b.startDate ASC", Booking.class)
            .setParameter("car", car)
            .setParameter("now", java.time.LocalDateTime.now())
            .getResultList();
        em.close();
        return bookings;
    }
}
