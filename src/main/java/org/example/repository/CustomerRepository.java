
package org.example.repository;

import org.example.model.Customer;
import jakarta.persistence.EntityManager;
import org.example.util.JPAUtil;
import java.util.List;
import java.util.Optional;

public class CustomerRepository {

    public void save(Customer customer) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (customer.getId() == null) {
                em.persist(customer);
            } else {
                em.merge(customer);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Optional<Customer> findByEmail(String email) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
        }
    }

    public List<Customer> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        }
    }
}
