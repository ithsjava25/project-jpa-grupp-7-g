package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entity.Car;
import org.example.service.BookingService;
import org.example.service.CarService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CentralHubController {

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, String> regColumn;

    @FXML
    private TableColumn<Car, String> availabilityColumn;

    @FXML
    private TableColumn<Car, String> nextAvailableColumn;

    private final CarService carService = new CarService();
    private final BookingService bookingService = new BookingService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        regColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));

        availabilityColumn.setCellValueFactory(cellData -> {
            Car car = cellData.getValue();
            boolean available = bookingService.isCarAvailable(car, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
            return new SimpleStringProperty(available ? "Tillgänglig" : "Bokad");
        });

        nextAvailableColumn.setCellValueFactory(cellData -> {
            Car car = cellData.getValue();
            LocalDateTime nextTime = bookingService.getNextAvailableTime(car);
            if (nextTime.isBefore(LocalDateTime.now()) || nextTime.isEqual(LocalDateTime.now())) {
                return new SimpleStringProperty("Nu");
            }
            return new SimpleStringProperty(nextTime.format(formatter));
        });

        carTable.getItems().setAll(carService.getAllCars());
    }
}
