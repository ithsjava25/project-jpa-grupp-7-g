package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.config.JpaUtil;
import org.example.entity.Booking;
import org.example.entity.Payment;
import org.example.entity.PaymentStatus;
import org.example.entity.PaymentMethod;
import java.math.BigDecimal;

public class PaymentService {

    public void testCreatePayments() {

        Payment payment1 = new Payment(new BigDecimal("5000.00"), 30, PaymentMethod.INVOICE);

        Payment payment2 = new Payment(new BigDecimal("10000.00"), 90, PaymentMethod.INVOICE);

        payment1.markAsPaid();

        if (payment2.isOverdue()) {
            System.out.println("Fakturan är försenad!");
        }

        long daysLeft = payment2.getDaysUntilDue();
        System.out.println("Dagar kvar: " + daysLeft);
    }

    public Payment createPayment(BigDecimal amount, int days, PaymentMethod method) {
        return new Payment(amount, days, method);
    }

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
