package org.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("car_rental_pu");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
