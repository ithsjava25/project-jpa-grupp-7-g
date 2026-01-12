package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.example.entity.PaymentMethod;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

public class EmailService {

    private final String from = "ericthilen@gmail.com";
    private final String password = "honlndeywwkfdvri"; // App-lösenord utan mellanslag

    /**
     * Startar en bakgrundstråd som lyssnar efter inkommande mejl (avbokningar).
     */
    public void startEmailListener(BookingService bookingService) {

        if (isUsingPlaceholder()) {
            System.out.println("[DEBUG_LOG] Email listener startar inte i testläge.");
            return;
        }

        new Thread(() -> {
            System.out.println("[DEBUG_LOG] Email listener startad...");

            while (true) {
                try {
                    Properties props = new Properties();
                    props.put("mail.store.protocol", "imaps");
                    props.put("mail.imaps.host", "imap.gmail.com");
                    props.put("mail.imaps.port", "993");
                    props.put("mail.imaps.ssl.enable", "true");
                    // Lägg till timeout för att undvika hängande anslutningar
                    props.put("mail.imaps.timeout", "10000");
                    props.put("mail.imaps.connectiontimeout", "10000");

                    Session session = Session.getInstance(props);
                    Store store = session.getStore("imaps");

                    try {
                        store.connect("imap.gmail.com", from, password);
                    } catch (AuthenticationFailedException e) {
                        System.err.println("[ERROR] Email listener: Autentisering misslyckades för " + from + ". Kontrollera applösenord.");
                        Thread.sleep(60000); // Vänta längre vid auth-fel
                        continue;
                    }

                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_WRITE);

                    Message[] messages = inbox.search(
                        new jakarta.mail.search.FlagTerm(
                            new Flags(Flags.Flag.SEEN), false
                        )
                    );

                    for (Message msg : messages) {
                        processIncomingEmail(msg, bookingService, session);
                        msg.setFlag(Flags.Flag.SEEN, true);
                    }

                    inbox.close(false);
                    store.close();

                } catch (Exception e) {
                    System.err.println("[ERROR] Fel i email listener: " + e.getMessage());
                }

                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    private void processIncomingEmail(Message message,
                                      BookingService bookingService,
                                      Session session) {

        try {
            String subject = message.getSubject();
            String content = getEmailContent(message).toLowerCase();
            String fromEmail =
                ((InternetAddress) message.getFrom()[0]).getAddress();

            System.out.println("[DEBUG_LOG] Nytt mejl från: " + fromEmail +
                " Ämne: " + subject);

            if (subject != null &&
                subject.contains("EVTA Rental") &&
                subject.contains("#") &&
                content.contains("avboka")) {

                String bookingIdStr =
                    subject.substring(subject.lastIndexOf("#") + 1).trim();
                Long bookingId = Long.parseLong(bookingIdStr);

                String result = bookingService.cancelBooking(bookingId);

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");

                Session replySession = Session.getInstance(
                    props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, password);
                        }
                    }
                );

                sendAutoReply(fromEmail, result, bookingId, replySession);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Kunde inte processa inkommande mejl: " + e.getMessage());
        }
    }

    private String getEmailContent(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) message.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                BodyPart bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    return bp.getContent().toString();
                }
            }
        }
        return "";
    }

    private void sendAutoReply(String toEmail,
                               String result,
                               Long bookingId,
                               Session session) {

        try {
            Message reply = new MimeMessage(session);
            reply.setFrom(new InternetAddress(from));
            reply.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
            );
            reply.setSubject(
                "Re: EVTA Rental - Bokningsbekräftelse #" + bookingId
            );

            String text;
            if (result.contains("framgångsrikt")) {
                text = "Hej, din bokning #" + bookingId +
                    " har nu avbokats framgångsrikt.";
            } else if (result.contains("24 timmar")) {
                text = "Hej, tyvärr går det inte att avboka " +
                    "då det är mindre än 24h kvar.";
            } else {
                text = "Hej, vi kunde inte hantera din avbokning: " + result;
            }

            reply.setText(text);
            Transport.send(reply);

            System.out.println("[DEBUG_LOG] Autosvar skickat till " + toEmail);

        } catch (Exception e) {
            System.err.println("[ERROR] Kunde inte skicka autosvar: " + e.getMessage());
        }
    }

    /**
     * Skickar avbokningsmejl via e-post.
     */
    public void sendCancellationEmail(String toEmail, String customerName) {
        if (isUsingPlaceholder()) {
            System.out.println("[DEBUG_LOG] Avbokningsmejl skickas inte i testläge.");
            return;
        }
        new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("EVTA Rental - Avbokning");

                String text = "Hej " + customerName + "!\n\n" +
                    "Vi har behövt avboka din hyrning av vår bil. Vänligen återkom till oss om du vill boka om på nytt.\n\n" +
                    "Med vänliga hälsningar,\nEVTA Rental";

                message.setText(text);
                Transport.send(message);

                System.out.println("[DEBUG_LOG] Avbokningsmejl skickat till " + toEmail);

            } catch (Exception e) {
                System.err.println("[ERROR] Kunde inte skicka avbokningsmejl: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public boolean isUsingPlaceholder() {
        return from.contains("dinadress") ||
            password.equals("ditt-app-lösenord");
    }

    /**
     * Skickar bokningsbekräftelse via e-post.
     */
    public void sendBookingConfirmation(String toEmail,
                                        String customerName,
                                        PaymentMethod method,
                                        double totalPrice,
                                        Label statusLabel,
                                        Long bookingId) {

        if (isUsingPlaceholder()) {
            System.out.println("[DEBUG_LOG] Bokningsbekräftelse skickas inte i testläge.");
            Platform.runLater(() -> {
                statusLabel.setText("Bokning sparad (testläge, inget mejl skickat).");
                statusLabel.setStyle("-fx-text-fill: blue;");
            });
            return;
        }

        new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(
                props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                }
            );

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
                );
                message.setSubject(
                    "EVTA Rental - Bokningsbekräftelse #" + bookingId
                );

                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();

                if (method == PaymentMethod.INVOICE) {
                    textPart.setText(
                        "Hej " + customerName + ",\n\n" +
                            "Tack för din bokning. Faktura finns bifogad.\n\n" +
                            "Med vänliga hälsningar,\nEVTA Rental"
                    );
                    multipart.addBodyPart(textPart);

                    File invoiceFile = new File("faktura.txt");
                    try (PrintWriter writer = new PrintWriter(invoiceFile)) {
                        writer.println("FAKTURA - EVTA Rental");
                        writer.println("Kund: " + customerName);
                        writer.println("Att betala: " + totalPrice + " kr");
                    }

                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.attachFile(invoiceFile);
                    multipart.addBodyPart(attachment);

                } else {
                    textPart.setText(
                        "Hej " + customerName + ",\n\n" +
                            "Tack för din bokning. Betalning sker vid ankomst.\n\n" +
                            "Med vänliga hälsningar,\nEVTA Rental"
                    );
                    multipart.addBodyPart(textPart);
                }

                message.setContent(multipart);
                Transport.send(message);

                Platform.runLater(() -> {
                    statusLabel.setText("Bokning bekräftad och mejl skickat!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    statusLabel.setText(
                        "Bokning sparad, men mejl kunde inte skickas."
                    );
                    statusLabel.setStyle("-fx-text-fill: orange;");
                });
            }
        }).start();
    }
}
