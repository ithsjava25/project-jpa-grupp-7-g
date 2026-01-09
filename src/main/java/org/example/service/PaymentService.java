package org.example.service;

import org.example.entity.Booking;
import org.example.entity.Payment;
import org.example.repository.PaymentRepository;

public class PaymentService {

    private final PaymentRepository repo = new PaymentRepository();

    public void savePayment(String firstName, String lastName, String email, String personalNumber, Booking booking) {
        Payment payment = new Payment(firstName, lastName, email, personalNumber, booking);
        repo.save(payment);
    }
}
