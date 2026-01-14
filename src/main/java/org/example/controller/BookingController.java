package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.model.Car;
import org.example.model.Extra;
import org.example.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingController {

    @FXML private ComboBox<Car> carComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField customerEmailField;
    @FXML private VBox extrasContainer;
    @FXML private Label daysLabel;
    @FXML private Label carPriceLabel;
    @FXML private Label totalAmountLabel;

    private List<CheckBox> extraCheckBoxes = new ArrayList<>();

    @FXML
    public void initialize() {
        loadExtras();
        loadAvailableCars(); // <--- Anropa den nya metoden här
    }

    private void loadAvailableCars() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Hämtar alla bilar som är markerade som tillgängliga
            List<Car> availableCars = em.createQuery("SELECT c FROM Car c WHERE c.available = true", Car.class).getResultList();
            carComboBox.getItems().setAll(availableCars);
        } finally {
            em.close();
        }
    }

    private void loadExtras() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            List<Extra> allExtras = em.createQuery("SELECT e FROM Extra e", Extra.class).getResultList();

            extrasContainer.getChildren().clear();
            extraCheckBoxes.clear();

            for (Extra extra : allExtras) {
                CheckBox cb = new CheckBox(extra.getName() + " (" + extra.getPrice() + " kr)");
                cb.setUserData(extra);
                cb.setOnAction(e -> updatePriceDisplay()); // Uppdatera totalpris när man klickar
                extraCheckBoxes.add(cb);
                extrasContainer.getChildren().add(cb);
            }
        } finally {
            em.close();
        }
    }

    @FXML
    public void updatePriceDisplay() {
        Car selectedCar = carComboBox.getSelectionModel().getSelectedItem();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        long days = 0;
        double carTotal = 0;
        double extrasTotal = 0;

        // 1. Räkna ut antal dagar
        if (start != null && end != null && !end.isBefore(start)) {
            days = ChronoUnit.DAYS.between(start, end);
            if (days == 0) days = 1; // Räkna påbörjad dag som 1 dag
        }

        // 2. Räkna ut bilens pris
        if (selectedCar != null) {
            carTotal = selectedCar.getDailyPrice() * days;
        }

        // 3. Räkna ut tilläggens pris
        extrasTotal = extraCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .mapToDouble(cb -> ((Extra) cb.getUserData()).getPrice())
                .sum();

        // 4. Uppdatera labels i UI
        daysLabel.setText(String.valueOf(days));
        carPriceLabel.setText(String.format("%.2f kr", carTotal));

        double total = carTotal + extrasTotal;
        totalAmountLabel.setText(String.format("%.2f kr", total));
    }

    @FXML
    public void onConfirmBooking() {
        // Kontrollera att allt är ifyllt
        if (carComboBox.getValue() == null || startDatePicker.getValue() == null || customerEmailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Vänligen fyll i alla uppgifter!");
            alert.show();
            return;
        }

        // HÄR SKER SJÄLVA SPARANDET I DATABASEN (Logik läggs till i BookingService sen)

        // Tack-meddelande
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bokning Bekräftad");
        alert.setHeaderText("Tack för din bokning!");
        alert.setContentText("Din bil är nu reserverad. Välkommen att hämta den på valt startdatum.");

        alert.showAndWait();

        // Rensa formuläret eller hoppa tillbaka till huvudvyn
        clearForm();
    }

    private void clearForm() {
        carComboBox.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        customerEmailField.clear();
        extraCheckBoxes.forEach(cb -> cb.setSelected(false));
        updatePriceDisplay();
    }

    public void setSelectedCar(Car car) {
        if (carComboBox != null && car != null) {
            carComboBox.getSelectionModel().select(car);
            updatePriceDisplay();
        }
    }
}
