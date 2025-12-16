package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =
            new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));

        Scene scene = new Scene(loader.load());
        stage.setTitle("Car Rental System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("carRentalPU");
        EntityManager em = emf.createEntityManager();

        // Starta transaktion
        em.getTransaction().begin();

        // Skapa och spara en bil
        Car car = new Car();
        car.setBrand("Volvo");
        car.setModel("XC90");
        car.setYear(2025);
        em.persist(car);

        // Commit
        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Car saved with ID: " + car.getId());

 */
    }
        }
