package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainViewController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void showCarView() {
        loadView("/fxml/car_view.fxml");
    }

    @FXML
    public void showBookingView() {
        loadView("/fxml/booking_view.fxml");
    }

    @FXML
    public void showCustomerView() {
        loadView("/fxml/customer_view.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Kunde inte ladda vyn: " + fxmlPath);
        }
    }
}


