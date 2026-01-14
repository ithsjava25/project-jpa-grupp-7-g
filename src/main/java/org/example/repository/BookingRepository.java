package org.example.repository;

import org.example.model.Booking;
import jakarta.persistence.EntityManager;
import org.example.util.JPAUtil;
import java.util.List;

public class BookingRepository {

    public void save(Booking booking) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (booking.getId() == null) {
                em.persist(booking);
            } else {
                em.merge(booking);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Booking> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT b FROM Booking b", Booking.class).getResultList();
        }
    }
}
