package org.example.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = isTestMode() ? "car_rental_test_pu" : "car_rental_pu";

    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private static boolean isTestMode() {
        try {
            Class.forName("org.junit.jupiter.api.Test");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
