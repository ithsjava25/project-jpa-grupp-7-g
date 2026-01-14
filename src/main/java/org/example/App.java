package org.example;

import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.util.DataInitializer;
import org.example.util.JPAUtil;

public class App extends Application {
// ... existing code ...

    @Override
    public void start(Stage stage) {
        try {
            // 1. Initiera databasen och ladda testdata (seed)
            initDatabase();

            // 2. Ladda gränssnittet
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/main_view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

            // Koppla på CSS för "Hertz-looken"
           // scene.getStylesheets().add(App.class.getResource("/css/main.css").toExternalForm());

            stage.setTitle("Hertz Biluthyrning - Grupp 7");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Kunde inte starta applikationen: " + e.getMessage());
        }
    }

    private void initDatabase() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            DataInitializer.seed(em);
        } finally {
            em.close();
        }
    }

    @Override
    public void stop() {
        // Stäng ner JPA snyggt när appen stängs
        JPAUtil.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}
