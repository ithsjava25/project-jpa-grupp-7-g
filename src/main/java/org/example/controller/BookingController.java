package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.model.*;
import org.example.service.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @FXML private ComboBox<Car> carComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField customerEmailField;
    @FXML private Label daysLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label carPriceLabel;

    private final BookingService bookingService = new BookingService();
    private final CarService carService = new CarService();
    private final CustomerService customerService = new CustomerService();

    @FXML
    public void initialize() {
        // Ladda tillgängliga bilar i dropdownen
        carComboBox.getItems().setAll(carService.getAvailableCars());

        // Sätt dagens datum som default startdatum
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));

        updatePriceDisplay();
    }

    @FXML
    public void updatePriceDisplay() {
        Car selectedCar = carComboBox.getValue();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (selectedCar != null && start != null && end != null) {
            Booking tempBooking = new Booking(null, selectedCar, start, end);
            long days = tempBooking.getDurationInDays();
            double total = bookingService.calculateTotalPrice(tempBooking);

            daysLabel.setText(String.valueOf(days));
            carPriceLabel.setText(String.format("%.2f kr", selectedCar.getDailyPrice() * days));
            totalAmountLabel.setText(String.format("%.2f kr", total));
        }
    }

    @FXML
    public void onConfirmBooking() {
        Car car = carComboBox.getValue();
        String email = customerEmailField.getText();

        if (car == null || email.isEmpty()) {
            showError("Vänligen fyll i alla uppgifter.");
            return;
        }

        // Hämta eller skapa kund
        Customer customer = customerService.findCustomerByEmail(email)
                .orElse(new Customer("Okänd", "Kund", email));

        if (customer.getId() == null) customerService.saveCustomer(customer);

        Booking booking = new Booking(customer, car, startDatePicker.getValue(), endDatePicker.getValue());

        try {
            bookingService.createBooking(booking);
            showInfo("Bokning bekräftad!", "Bilen är nu reserverad.");
        } catch (Exception e) {
            showError("Kunde inte skapa bokning: " + e.getMessage());
        }
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
