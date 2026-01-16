package org.example.repository;

// AddonRepository - CRUD operations for Addon entity

import org.example.config.JpaUtil;
import org.example.entity.Addon;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AddonRepository {

    public List<Addon> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        return em.createQuery("SELECT a FROM Addon a", Addon.class).getResultList();
    }
}
