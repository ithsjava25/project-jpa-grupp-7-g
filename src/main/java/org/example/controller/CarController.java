package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.beans.property.SimpleStringProperty;
import org.example.model.Car;
import org.example.model.CarType;
import org.example.service.CarService;
import java.io.IOException;

public class CarController {

    @FXML private TableView<Car> carTable;
    @FXML private TableColumn<Car, String> brandColumn;
    @FXML private TableColumn<Car, String> modelColumn;
    @FXML private TableColumn<Car, CarType> typeColumn;
    @FXML
    private TableColumn<Car, Double> priceColumn;
    @FXML
    private TableColumn<Car, String> statusColumn;
    @FXML
    private TextField searchField;

    private final CarService carService = new CarService();

    @FXML
    public void initialize() {
        // Koppla kolumnerna till Car-modellens fält via getters
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("dailyPrice"));

        // Specialhantering för status-kolumnen för att visa ikoner/text istället för boolean
        statusColumn.setCellValueFactory(cellData -> {
            boolean available = cellData.getValue().isAvailable();
            boolean damaged = cellData.getValue().isDamaged();

            if (damaged) return new SimpleStringProperty("🛠 Trasig");
            return new SimpleStringProperty(available ? "✅ Tillgänglig" : "❌ Uthyrd");
        });

        loadCars();
    }

    private void loadCars() {
        // Vi hämtar alla bilar för att visa hela vagnparken i tabellen
        carTable.getItems().setAll(carService.getAllCars());
    }

    @FXML
    public void handleBookingAction() {
        Car selectedCar = carTable.getSelectionModel().getSelectedItem();
        if (selectedCar != null && selectedCar.isAvailable() && !selectedCar.isDamaged()) {
            try {
                // 1. Ladda bokningsvyn
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking_view.fxml"));
                Parent bookingView = loader.load();

                // 2. Skicka med den valda bilen till BookingController (valfritt men bra!)
                BookingController bookingController = loader.getController();
                bookingController.setSelectedCar(selectedCar);

                // 3. Hitta contentArea och byt ut innehållet
                StackPane contentArea = (StackPane) carTable.getScene().lookup("#contentArea");
                if (contentArea != null) {
                    contentArea.getChildren().setAll(bookingView);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
