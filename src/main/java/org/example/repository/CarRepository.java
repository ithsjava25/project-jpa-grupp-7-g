package org.example.repository;

import org.example.model.Car;
import org.example.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class CarRepository {

    public List<Car> findAll() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Car> cars =
            em.createQuery("SELECT c FROM Car c", Car.class)
                .getResultList();

        em.close();
        return cars;
    }
}
