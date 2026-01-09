package org.example.controller;

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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingController {

    @FXML
    private ComboBox<Car> carComboBox;

    @FXML
    private RadioButton hourlyRadio;

    @FXML
    private RadioButton dailyRadio;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private VBox addonsVBox;

    @FXML
    private Label priceLabel;

    @FXML
    private Label statusLabel;

    private final CarService carService = new CarService();
    private final BookingService bookingService = new BookingService();
    private final AddonService addonService = new AddonService();

    private final ToggleGroup bookingTypeGroup = new ToggleGroup();
    private final List<CheckBox> addonCheckBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        carComboBox.getItems().setAll(carService.getAllCars());

        hourlyRadio.setToggleGroup(bookingTypeGroup);
        dailyRadio.setToggleGroup(bookingTypeGroup);
        dailyRadio.setSelected(true);

        List<Addon> addons = addonService.getAllAddons();
        for (Addon addon : addons) {
            CheckBox cb = new CheckBox(addon.toString());
            cb.setUserData(addon);
            cb.setOnAction(e -> updatePrice());
            addonCheckBoxes.add(cb);
            addonsVBox.getChildren().add(cb);
        }

        carComboBox.setOnAction(e -> updatePrice());
        hourlyRadio.setOnAction(e -> updatePrice());
        dailyRadio.setOnAction(e -> updatePrice());
        startDatePicker.setOnAction(e -> updatePrice());
        endDatePicker.setOnAction(e -> updatePrice());
    }

    private void updatePrice() {
        if (canCalculatePrice()) {
            double price = bookingService.calculatePrice(
                    carComboBox.getValue(),
                    startDatePicker.getValue().atStartOfDay(),
                    endDatePicker.getValue().atTime(LocalTime.MAX),
                    getSelectedType(),
                    getSelectedAddons()
            );
            priceLabel.setText(String.format("%.2f kr", price));
        }
    }

    private boolean canCalculatePrice() {
        return carComboBox.getValue() != null &&
                startDatePicker.getValue() != null &&
                endDatePicker.getValue() != null &&
                !endDatePicker.getValue().isBefore(startDatePicker.getValue());
    }

    private BookingType getSelectedType() {
        return hourlyRadio.isSelected() ? BookingType.HOURLY : BookingType.DAILY;
    }

    private List<Addon> getSelectedAddons() {
        List<Addon> selected = new ArrayList<>();
        for (CheckBox cb : addonCheckBoxes) {
            if (cb.isSelected()) {
                selected.add((Addon) cb.getUserData());
            }
        }
        return selected;
    }

    @FXML
    private void handleBooking() {
        if (!canCalculatePrice()) {
            statusLabel.setText("Vänligen fyll i alla fält korrekt.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Car car = carComboBox.getValue();
        LocalDateTime start = startDatePicker.getValue().atStartOfDay();
        LocalDateTime end = endDatePicker.getValue().atTime(LocalTime.MAX);
        BookingType type = getSelectedType();
        List<Addon> addons = getSelectedAddons();

        if (!bookingService.isCarAvailable(car, start, end)) {
            statusLabel.setText("Bilen är tyvärr inte tillgänglig under denna period.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        double price = bookingService.calculatePrice(car, start, end, type, addons);

        Booking booking = new Booking(start, end, type, car);
        booking.setAddons(addons);
        booking.setTotalPrice(price);

        try {
            bookingService.saveBooking(booking);
            statusLabel.setText("Bokning bekräftad!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            statusLabel.setText("Ett fel uppstod vid bokning.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
