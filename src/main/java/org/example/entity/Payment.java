
package org.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "payment")
    private Booking booking;

    private BigDecimal amount;
    private LocalDate invoiceDate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    public Payment() {
    }

    public Payment(BigDecimal amount, int daysToPayInDays) {
        this.amount = amount;
        this.invoiceDate = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(daysToPayInDays);
        this.status = PaymentStatus.PENDING;
    }

    public Payment(BigDecimal amount, int daysToPayInDays, PaymentMethod method) {
        this(amount, daysToPayInDays);
        this.method = method;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void markAsPaid() {
        this.status = PaymentStatus.COMPLETED;
    }

    public boolean isOverdue() {
        if (dueDate == null) return false;
        boolean isAfterDueDate = LocalDate.now().isAfter(getDueDate());
        boolean isNotPaid = status != PaymentStatus.COMPLETED;
        return isAfterDueDate && isNotPaid;
    }

    public long getDaysUntilDue() {
        if (dueDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), getDueDate());
    }
}
