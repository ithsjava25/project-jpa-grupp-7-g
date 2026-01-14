package org.example;

import org.example.entity.Payment;
import org.example.entity.PaymentMethod;
import org.example.entity.PaymentStatus;
import org.example.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tester för PaymentService (nybörjarvänlig)")
class PaymentServiceTest {

    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        paymentService = new PaymentService();
    }

    @Test
    @DisplayName("Skapa betalning med faktura")
    void shouldCreateInvoicePayment() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("5000.00"),
            30,
            PaymentMethod.INVOICE
        );

        assertThat(payment).isNotNull();
        assertThat(payment.getAmount()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(payment.getMethod()).isEqualTo(PaymentMethod.INVOICE);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Skapa betalning med kort")
    void shouldCreateCardPayment() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("2500.50"),
            0,
            PaymentMethod.CARD
        );

        assertThat(payment.getMethod()).isEqualTo(PaymentMethod.CARD);
    }

    @Test
    @DisplayName("Skapa betalning med kontanter")
    void shouldCreateCashPayment() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("1500.00"),
            0,
            PaymentMethod.CASH
        );

        assertThat(payment.getMethod()).isEqualTo(PaymentMethod.CASH);
    }

    @Test
    @DisplayName("Markera betalning som betald")
    void shouldMarkPaymentAsPaid() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("3000.00"),
            30,
            PaymentMethod.INVOICE
        );

        paymentService.payPayment(payment);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    @DisplayName("Redan betald betalning ändras inte")
    void shouldNotPayAlreadyPaidPayment() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("5000.00"),
            30,
            PaymentMethod.INVOICE
        );

        payment.markAsPaid();
        payment.markAsPaid(); // betala igen

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    @DisplayName("Betalning före förfallodatum är inte försenad")
    void paymentIsNotOverdue() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("1000.00"),
            10,
            PaymentMethod.INVOICE
        );

        assertThat(payment.isOverdue()).isFalse();
    }

    @Test
    @DisplayName("Betald betalning räknas inte som försenad")
    void paidPaymentIsNotOverdue() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("2000.00"),
            -5,
            PaymentMethod.INVOICE
        );

        payment.markAsPaid();

        assertThat(payment.isOverdue()).isFalse();
    }

    @Test
    @DisplayName("Räkna dagar kvar till betalning")
    void shouldCalculateDaysUntilDue() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("5000.00"),
            30,
            PaymentMethod.INVOICE
        );

        long daysLeft = payment.getDaysUntilDue();

        assertThat(daysLeft).isBetween(29L, 30L);
    }

    @Test
    @DisplayName("Försenad betalning ger negativa dagar")
    void overduePaymentHasNegativeDays() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("1000.00"),
            -10,
            PaymentMethod.INVOICE
        );

        assertThat(payment.getDaysUntilDue()).isLessThan(0);
    }

    @Test
    @DisplayName("Fakturadatum sätts till idag")
    void invoiceDateIsToday() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("1000.00"),
            30,
            PaymentMethod.INVOICE
        );

        assertThat(payment.getInvoiceDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Förfallodatum sätts rätt")
    void dueDateIsCorrect() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("5000.00"),
            30,
            PaymentMethod.INVOICE
        );

        assertThat(payment.getDueDate())
            .isEqualTo(LocalDate.now().plusDays(30));
    }

    @Test
    @DisplayName("Skapa → betala → kontrollera")
    void completePaymentFlow() {
        Payment payment = paymentService.createPayment(
            new BigDecimal("4500.00"),
            30,
            PaymentMethod.INVOICE
        );

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);

        paymentService.payPayment(payment);

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.isOverdue()).isFalse();
    }
}
