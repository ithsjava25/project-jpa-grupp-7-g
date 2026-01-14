package org.example.repository;

import org.example.model.Car;
import jakarta.persistence.EntityManager;
import org.example.util.JPAUtil;
import java.util.List;

public class CarRepository {

    public List<Car> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Car c", Car.class).getResultList();
        }
    }

    public List<Car> findAvailableCars() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Car c WHERE c.available = true AND c.damaged = false", Car.class)
                    .getResultList();
        }
    }

    public void save(Car car) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (car.getId() == null) {
                em.persist(car);
            } else {
                em.merge(car);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
