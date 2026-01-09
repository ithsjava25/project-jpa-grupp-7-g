package org.example.repository;

import jakarta.persistence.EntityManager;
import org.example.config.JpaUtil;
import org.example.entity.Payment;

public class PaymentRepository {

    public void save(Payment payment) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(payment);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
