package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        showCentralHub();
    }

    @FXML
    private void showCentralHub() {
        load("/fxml/central-hub.fxml");
    }

    @FXML
    private void showBookingView() {
        load("/fxml/booking-view.fxml");
    }

    private void load(String path) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(path));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
