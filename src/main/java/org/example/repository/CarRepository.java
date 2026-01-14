package org.example.repository;

import org.example.config.JpaUtil;
import org.example.entity.Car;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CarRepository {

    public List<Car> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Car c", Car.class).getResultList();
        } finally {
            em.close();
        }
    }
}
