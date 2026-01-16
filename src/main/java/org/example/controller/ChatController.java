package org.example.controller;

// ChatController - Controller for chat window

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.service.CarService;
import org.example.entity.Car;
import java.util.List;
import java.util.stream.Collectors;

public class ChatController {

    @FXML private VBox chatHistory;
    @FXML private ScrollPane chatScrollPane;
    @FXML private FlowPane questionsPane;

    private final CarService carService = new CarService();

    @FXML
    public void initialize() {
        addQuestion("Vilka är vi?", "Vi är ett biluthyrningsföretag där man kan hyra våra bilar som har utgångspunkt i Göteborg.");
        addQuestion("Vilka bilar erbjuder vi?", getCarsResponse());
        addQuestion("Vad har vi för tillägg?", "Vi har takbox, barnstol & försäkring");
        addQuestion("Vad kostar det att hyra en bil?", "Priserna varierar men vi har ett pris från 80 kronor / timme");
        addQuestion("Hur bokar jag?", "Du klickar på 'Boka bil' i menyn till vänster!");
    }

    private void addQuestion(String question, String answer) {
        Button btn = new Button(question);
        btn.getStyleClass().add("button");
        btn.setOnAction(e -> {
            addUserMessage(question);
            addAiMessage(answer);
        });
        questionsPane.getChildren().add(btn);
    }

    private void addUserMessage(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("chat-bubble-user");
        label.setWrapText(true);
        chatHistory.getChildren().add(label);
        scrollToBottom();
    }

    private void addAiMessage(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("chat-bubble-ai");
        label.setWrapText(true);
        chatHistory.getChildren().add(label);
        scrollToBottom();
    }

    private String getCarsResponse() {
        List<Car> cars = carService.getAllCars();
        return "Vi erbjuder följande bilar: " +
            cars.stream()
                .map(c -> c.getBrand() + " " + c.getModel())
                .collect(Collectors.joining(", "));
    }

    private void scrollToBottom() {
        chatHistory.layout();
        chatScrollPane.setVvalue(1.0);
    }
}
