package org.example.repository;

// BookingRepository - CRUD operations for Booking entity

import org.example.config.JpaUtil;
import org.example.entity.Booking;
import jakarta.persistence.EntityManager;
import java.util.List;

public class BookingRepository {

    public List<Booking> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Booking> results = em.createQuery("SELECT b FROM Booking b ORDER BY b.id DESC", Booking.class).getResultList();
            System.out.println("[DEBUG_LOG] Hämtade " + results.size() + " bokningar från databasen.");
            return results;
        } finally {
            em.close();
        }
    }

    public void save(Booking booking) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (booking.getId() == null) {
                em.persist(booking);
            } else {
                em.merge(booking);
            }
            em.getTransaction().commit();
            System.out.println("[DEBUG_LOG] Bokning sparad med ID: " + booking.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Booking findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Booking.class, id);
        } finally {
            em.close();
        }
    }

    public boolean isCarAvailable(org.example.entity.Car car, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(b) FROM Booking b " +
                    "WHERE b.car = :car " +
                    "AND b.status = :status " +
                    "AND ((b.startDate < :end AND b.endDate > :start))", Long.class)
                .setParameter("car", car)
                .setParameter("status", org.example.entity.BookingStatus.ACTIVE)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();
            return count == 0;
        } finally {
            em.close();
        }
    }

    public List<Booking> findFutureBookingsByCar(org.example.entity.Car car) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            List<Booking> bookings = em.createQuery(
                    "SELECT b FROM Booking b " +
                    "WHERE b.car = :car AND b.endDate > :now AND b.status = :status " +
                    "ORDER BY b.startDate ASC", Booking.class)
                .setParameter("car", car)
                .setParameter("now", java.time.LocalDateTime.now())
                .setParameter("status", org.example.entity.BookingStatus.ACTIVE)
                .getResultList();
            return bookings;
        } finally {
            em.close();
        }
    }
}
