package org.example.controller;

// MainController - Controller for the main window

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import org.example.service.BookingService;
import org.example.service.EmailService;

public class MainController {

    @FXML
    private StackPane contentArea;

    private final EmailService emailService = new EmailService();
    private final BookingService bookingService = new BookingService();

    @FXML
    public void initialize() {
        showCentralHub();
        emailService.startEmailListener(bookingService);
    }

    @FXML
    private void showCentralHub() {
        load("/fxml/central-hub.fxml");
    }

    @FXML
    private void showBookingView() {
        load("/fxml/booking-view.fxml");
    }

    @FXML
    private void showChat() {
        load("/fxml/chat-view.fxml");
    }

    @FXML
    private void showMyBookings() {
        load("/fxml/my-bookings-view.fxml");
    }

    private void load(String path) {
        try {
            System.out.println("Loading FXML: " + path);
            var resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("Could not find FXML file: " + path);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Node node = loader.load();
            contentArea.getChildren().setAll(node);
            System.out.println("Successfully loaded: " + path);
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + path);
            e.printStackTrace();
        }
    }
}
