package org.example;

import org.junit.jupiter.api.Test;
import org.example.service.EmailService;
import org.example.entity.PaymentMethod;
import javafx.scene.control.Label;

import static org.assertj.core.api.Assertions.assertThat;

class EmailSendingTest {

    private final EmailService emailService = new EmailService();

    @Test
    void testBookingConfirmationEmailSending() {
        String testEmail = "test@example.com";
        String customerName = "Test Kund";
        double totalPrice = 500.0;
        Long bookingId = 123L;
        Label statusLabel = new Label();
        //kolla är med ERIK

        try {
            emailService.sendBookingConfirmation(
                testEmail,
                customerName,
                PaymentMethod.CARD,
                totalPrice,
                statusLabel,
                bookingId
            );

            Thread.sleep(2000);

            assertThat(statusLabel.getText())
                .as("Mejl borde ha skickats eller vara i testläge")
                .isNotEmpty();

            System.out.println(" Bokningsbekräftelse mejl test GODKÄNT!");
            System.out.println("   Status: " + statusLabel.getText());

        } catch (Exception e) {
            throw new AssertionError(" Mejl skickling misslyckades: " + e.getMessage(), e);
        }
    }

    @Test
    void testCancellationEmailSending() {
        String testEmail = "test@example.com";
        String customerName = "Test Kund";

        try {
            emailService.sendCancellationEmail(testEmail, customerName);

            Thread.sleep(2000);

            System.out.println(" Avbokningsmejl test GODKÄNT!");

        } catch (Exception e) {
            throw new AssertionError(" Avbokningsmejl skickling misslyckades: " + e.getMessage(), e);
        }
    }

    @Test
    void testEmailServicePlaceholder() {
        boolean isPlaceholder = emailService.isUsingPlaceholder();

        if (isPlaceholder) {
            System.out.println("⚠️  VARNING: EmailService använder placeholder-autentiseringsuppgifter!");
            System.out.println("   Du behöver uppdatera emailadress och applösenord i EmailService");
        } else {
            System.out.println("✅ EmailService använder riktiga autentiseringsuppgifter");
        }

        assertThat(isPlaceholder)
            .as("Du bör uppdatera emailadress och lösenord")
            .isFalse();
    }
}
