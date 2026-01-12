package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.config.JpaUtil;
import org.example.entity.Booking;
import org.example.entity.Payment;
import org.example.entity.PaymentStatus;
import org.example.entity.PaymentMethod;
import java.math.BigDecimal;

public class PaymentService {

    // Metod för att skapa och testa betalningar
    public void testCreatePayments() {

        // Skapa en betalning på 5000 kronor med 30 dagar att betala (FAKTURA)
        Payment payment1 = new Payment(new BigDecimal("5000.00"), 30, PaymentMethod.INVOICE);

        // Skapa en betalning på 10000 kronor med 90 dagar att betala
        Payment payment2 = new Payment(new BigDecimal("10000.00"), 90, PaymentMethod.INVOICE);

        // Markera som betald
        payment1.markAsPaid();

        // Kontrollera om den är försenad
        if (payment2.isOverdue()) {
            System.out.println("Fakturan är försenad!");
        }

        // Få antal dagar kvar
        long daysLeft = payment2.getDaysUntilDue();
        System.out.println("Dagar kvar: " + daysLeft);
    }

    // Andra användbara metoder
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

        // Hitta den sparade bokningen igen för att vara säker på att den är i rätt state
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
