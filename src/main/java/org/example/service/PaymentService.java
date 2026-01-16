package org.example.service;

// PaymentService - Service class for Payment entity

import jakarta.persistence.EntityManager;
import org.example.config.JpaUtil;
import org.example.entity.Booking;
import org.example.entity.Payment;
import org.example.entity.PaymentMethod;
import java.math.BigDecimal;

public class PaymentService {

    public void payPayment(Payment payment) {
        payment.markAsPaid();
        System.out.println("Betalning på " + payment.getAmount() + " med metod " + payment.getMethod() + " är nu betald!");
    }

    public void savePayment(String firstName, String lastName, String email, String personalNumber, Booking booking, PaymentMethod method) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();

        Booking managedBooking = em.find(Booking.class, booking.getId());

        int daysToPay = (method == PaymentMethod.INVOICE) ? 30 : 0;
        Payment payment = new Payment(BigDecimal.valueOf(managedBooking.getTotalPrice()), daysToPay, method);
        payment.setBooking(managedBooking);
        managedBooking.setPayment(payment);

        em.persist(payment);
        em.merge(managedBooking);

        em.getTransaction().commit();
        em.close();
    }
}
