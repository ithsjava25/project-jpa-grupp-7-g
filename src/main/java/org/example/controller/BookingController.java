package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.entity.Addon;
import org.example.entity.Booking;
import org.example.entity.BookingType;
import org.example.entity.Car;
import org.example.service.AddonService;
import org.example.service.BookingService;
import org.example.service.CarService;
import org.example.service.EmailService;
import org.example.service.PaymentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @FXML private ComboBox<Car> carComboBox;
    @FXML private RadioButton hourlyRadio;
    @FXML private RadioButton dailyRadio;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private VBox addonsVBox;
    @FXML private Label priceLabel;
    @FXML private Label statusLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField personalNumberField;

    @FXML private VBox hourlyOptions;
    @FXML private VBox dailyOptions;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private VBox endDateBox;

    private final CarService carService = new CarService();
    private final BookingService bookingService = new BookingService();
    private final AddonService addonService = new AddonService();
    private final PaymentService paymentService = new PaymentService();
    private final EmailService emailService = new EmailService();

    private final ToggleGroup bookingTypeGroup = new ToggleGroup();
    private final List<CheckBox> addonCheckBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("[DEBUG_LOG] INIT START");

        carComboBox.getItems().setAll(carService.getAllCars());

        hourlyRadio.setToggleGroup(bookingTypeGroup);
        dailyRadio.setToggleGroup(bookingTypeGroup);
        dailyRadio.setSelected(true);

        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 17));

        updateVisibility();

        hourlyRadio.setOnAction(e -> {
            updateVisibility();
            updatePrice();
        });
        dailyRadio.setOnAction(e -> {
            updateVisibility();
            updatePrice();
        });

        updatePrice(); // Initial price update

        List<Addon> addons = addonService.getAllAddons();
        for (Addon addon : addons) {
            CheckBox cb = new CheckBox(addon.toString());
            cb.setUserData(addon);
            cb.setOnAction(e -> updatePrice());
            addonCheckBoxes.add(cb);
            addonsVBox.getChildren().add(cb);
        }

        carComboBox.setOnAction(e -> updatePrice());
        startDatePicker.setOnAction(e -> updatePrice());
        endDatePicker.setOnAction(e -> updatePrice());
        startHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        endHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());

        System.out.println("[DEBUG_LOG] INIT END");
    }

    private void updateVisibility() {
        boolean hourly = hourlyRadio.isSelected();
        hourlyOptions.setVisible(hourly);
        hourlyOptions.setManaged(hourly);
        dailyOptions.setVisible(!hourly);
        dailyOptions.setManaged(!hourly);
        endDateBox.setVisible(true);
        endDateBox.setManaged(true);
    }

    private void updatePrice() {
        System.out.println("[DEBUG_LOG] updatePrice() called");
        if (!canCalculatePrice()) {
            priceLabel.setText("Välj bil och datum");
            return;
        }

        Car car = carComboBox.getValue();
        BookingType type = getSelectedType();

        LocalDateTime start;
        LocalDateTime end;

        if (type == BookingType.HOURLY) {
            start = startDatePicker.getValue().atTime(startHourSpinner.getValue(), 0);
            end = startDatePicker.getValue().atTime(endHourSpinner.getValue(), 0);
            if (end.isBefore(start) || end.isEqual(start)) {
                end = end.plusDays(1);
            }
        } else {
            start = startDatePicker.getValue().atTime(8, 0);
            if (endDatePicker.getValue() != null) {
                end = endDatePicker.getValue().atTime(8, 0);
            } else {
                end = startDatePicker.getValue().plusDays(1).atTime(8, 0);
            }
        }

        if (end.isBefore(start)) {
            priceLabel.setText("Felaktigt datumintervall");
            return;
        }

        double price = bookingService.calculatePrice(car, start, end, type, getSelectedAddons());
        priceLabel.setText(String.format("%.2f kr", price));
        System.out.println("[DEBUG_LOG] Price updated: " + priceLabel.getText());
    }

    private boolean canCalculatePrice() {
        boolean hasCar = carComboBox.getValue() != null;
        boolean hasDate = startDatePicker.getValue() != null;
        return hasCar && hasDate;
    }

    private BookingType getSelectedType() {
        return hourlyRadio.isSelected() ? BookingType.HOURLY : BookingType.DAILY;
    }

    private List<Addon> getSelectedAddons() {
        List<Addon> selected = new ArrayList<>();
        for (CheckBox cb : addonCheckBoxes) {
            if (cb.isSelected()) selected.add((Addon) cb.getUserData());
        }
        return selected;
    }

    private boolean customerInfoValid() {
        return !firstNameField.getText().isBlank()
            && !lastNameField.getText().isBlank()
            && !emailField.getText().isBlank()
            && !personalNumberField.getText().isBlank();
    }

    @FXML
    private void handleBooking() {
        System.out.println("[DEBUG_LOG] HANDLE BOOKING TRIGGERED");

        if (!canCalculatePrice()) {
            statusLabel.setText("Fyll i datum och bil.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!customerInfoValid()) {
            statusLabel.setText("Fyll i kunduppgifter.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Car car = carComboBox.getValue();
        BookingType type = getSelectedType();

        LocalDateTime start;
        LocalDateTime end;

        if (type == BookingType.HOURLY) {
            start = startDatePicker.getValue().atTime(startHourSpinner.getValue(), 0);
            end = startDatePicker.getValue().atTime(endHourSpinner.getValue(), 0);
            if (end.isBefore(start) || end.isEqual(start)) {
                end = end.plusDays(1);
            }
        } else {
            start = startDatePicker.getValue().atTime(8, 0);
            if (endDatePicker.getValue() != null) {
                end = endDatePicker.getValue().atTime(8, 0);
            } else {
                end = startDatePicker.getValue().plusDays(1).atTime(8, 0);
            }
        }

        if (end.isBefore(start) || end.isEqual(start)) {
            statusLabel.setText("Slutdatum måste vara efter startdatum.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!bookingService.isCarAvailable(car, start, end)) {
            statusLabel.setText("Bilen är inte tillgänglig.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        List<Addon> addons = getSelectedAddons();
        double price = bookingService.calculatePrice(car, start, end, type, addons);

        Booking booking = new Booking(start, end, type, car,
            firstNameField.getText(), lastNameField.getText(),
            emailField.getText(), personalNumberField.getText());
        booking.setAddons(addons);
        booking.setTotalPrice(price);

        try {
            bookingService.saveBooking(booking);
            paymentService.savePayment(
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                personalNumberField.getText(),
                booking
            );

            System.out.println("[DEBUG_LOG] Booking and Payment saved. Sending email...");

            // Skicka mejl i bakgrundstråd med statuslabel-feedback
            emailService.sendBookingConfirmation(
                emailField.getText(),
                firstNameField.getText(),
                statusLabel
            );

        } catch (Exception e) {
            System.err.println("[ERROR] Fel vid bokning: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Fel vid bokning.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
