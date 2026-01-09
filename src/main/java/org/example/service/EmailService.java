package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.application.Platform;

import java.util.Properties;
import javafx.scene.control.Label;

public class EmailService {

    private final String from = "ericthilen@gmail.com";
    private final String password = "honlndeywwkfdvri"; // Ditt nya app-lösenord utan mellanslag

    public boolean isUsingPlaceholder() {
        return from.contains("dinavändare") || password.equals("ditt-app-lösenord");
    }

    /**
     * Skickar bokningsbekräftelse via e-post.
     * Körs i bakgrundstråd så UI inte fryser.
     * @param toEmail mottagarens mejl
     * @param customerName kundens namn
     * @param statusLabel label i JavaFX som visar status
     */
    public void sendBookingConfirmation(String toEmail, String customerName, Label statusLabel) {

        new Thread(() -> {

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true"); // Visar full SMTP-logg i terminalen

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            try {
                System.out.println("[DEBUG_LOG] Försöker skicka mejl till: " + toEmail);

                if (isUsingPlaceholder()) {
                    System.out.println("[DEBUG_LOG] Testläge: mejlet skulle skickats till " + toEmail);
                    return;
                }

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("EVTA Rental - tack för din bokning");
                message.setText(
                    "Hej " + customerName + ",\n\n" +
                        "Tack för din bokning, härmed bifogar jag din faktura i detta mejl.\n\n" +
                        "Med vänliga hälsningar,\nEVTA Rental"
                );

                Transport.send(message);
                System.out.println("[DEBUG_LOG] Mejlet skickades!");

                // Uppdatera UI-label i JavaFX Thread
                Platform.runLater(() -> {
                    statusLabel.setText("Bokning bekräftad och mejl skickat!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });

            } catch (Exception e) {
                System.err.println("[ERROR] Kunde inte skicka mejl: " + e.getMessage());
                e.printStackTrace();

                // Uppdatera UI-label i JavaFX Thread vid fel
                Platform.runLater(() -> {
                    statusLabel.setText("Bokning sparad, men mejl kunde inte skickas.");
                    statusLabel.setStyle("-fx-text-fill: orange;");
                });
            }

        }).start(); // Kör allt i bakgrund
    }
}
