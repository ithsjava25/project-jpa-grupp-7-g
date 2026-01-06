package org.example;

import org.example.model.Car;               // ✅ Rätt import
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {
    public static void main(String[] args) {
        // Skapa EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("carRentalPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Rensa tabellen innan vi lägger in nya bilar
        em.createQuery("DELETE FROM Car").executeUpdate();

        // Skapa och persist 20 bilar med rimliga dagspriser
        em.persist(createCar("Volvo", "XC90", 2025, "Lyxbil", 800_000));
        em.persist(createCar("BMW", "7 Series", 2024, "Lyxbil", 1_000_000));
        em.persist(createCar("Audi", "A8", 2023, "Lyxbil", 900_000));
        em.persist(createCar("Toyota", "Corolla", 2022, "Personbil", 200_000));
        em.persist(createCar("Honda", "Civic", 2021, "Personbil", 190_000));
        em.persist(createCar("Ford", "Focus", 2022, "Personbil", 210_000));
        em.persist(createCar("Mercedes", "Sprinter", 2023, "Minibuss", 350_000));
        em.persist(createCar("Volkswagen", "Transporter", 2023, "Minibuss", 330_000));
        em.persist(createCar("Renault", "Kangoo", 2022, "Minibuss", 300_000));
        em.persist(createCar("Fiat", "500", 2023, "Småbil", 120_000));
        em.persist(createCar("Mini", "Cooper", 2022, "Småbil", 150_000));
        em.persist(createCar("Volkswagen", "Up!", 2022, "Småbil", 110_000));
        em.persist(createCar("Porsche", "911", 2025, "Sportbil", 1_500_000));
        em.persist(createCar("Ferrari", "Roma", 2024, "Sportbil", 2_000_000));
        em.persist(createCar("Lamborghini", "Huracan", 2025, "Sportbil", 2_500_000));
        em.persist(createCar("Tesla", "Model S", 2023, "Lyxbil", 900_000));
        em.persist(createCar("Kia", "Rio", 2022, "Småbil", 130_000));
        em.persist(createCar("Hyundai", "i30", 2023, "Personbil", 195_000));
        em.persist(createCar("Opel", "Vivaro", 2022, "Minibuss", 320_000));
        em.persist(createCar("Jaguar", "F-Type", 2024, "Sportbil", 1_600_000));

        em.getTransaction().commit();
        em.close();
        emf.close();

        System.out.println("20 cars saved successfully!");
    }

    // Skapar bil och sätter dagspris till 0,5% av priset
    private static Car createCar(String brand, String model, int year, String type, double price) {
        Car car = new Car();          // ✅ korrekt syntax
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setType(type);
        car.setPrice(price);
        car.setDailyPrice(price * 0.005); // 0,5% av totalpris
        return car;
    }
}



