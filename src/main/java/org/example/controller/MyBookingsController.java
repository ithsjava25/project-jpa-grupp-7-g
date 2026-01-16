package org.example.controller;

// MyBookingsController - Controller for the "My Bookings" window

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entity.Booking;
import org.example.entity.BookingStatus;
import org.example.service.BookingService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyBookingsController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Long> idColumn;
    @FXML private TableColumn<Booking, String> carColumn;
    @FXML private TableColumn<Booking, String> startColumn;
    @FXML private TableColumn<Booking, String> endColumn;
    @FXML private TableColumn<Booking, Double> priceColumn;
    @FXML private TableColumn<Booking, String> statusColumn;
    @FXML private TableColumn<Booking, Void> actionColumn;
    @FXML private Label messageLabel;

    private final BookingService bookingService = new BookingService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        carColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCar().getBrand() + " " + cellData.getValue().getCar().getModel()));
        startColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate().format(formatter)));
        endColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate().format(formatter)));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        addActionButtons();
        refreshTable();
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Avboka");

            {
                btn.setOnAction(event -> {
                    Booking booking = getTableView().getItems().get(getIndex());
                    handleCancel(booking);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    if (booking.getStatus() == BookingStatus.ACTIVE) {
                        setGraphic(btn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void handleCancel(Booking booking) {
        String result = bookingService.cancelBooking(booking.getId(), true);
        messageLabel.setText(result);
        if (result.contains("framgångsrikt")) {
            messageLabel.setStyle("-fx-text-fill: green;");
            refreshTable();
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void refreshTable() {
        List<Booking> bookings = bookingService.getAllBookings();
        bookingTable.getItems().setAll(bookings);
    }
}
