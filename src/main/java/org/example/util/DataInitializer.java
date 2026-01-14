package org.example.util;

import jakarta.persistence.EntityManager;
import org.example.model.*;

public class DataInitializer {

    public static void seed(EntityManager em) {


        if (!em.createQuery("SELECT c FROM Car c").getResultList().isEmpty()) {
            return;
        }

        em.getTransaction().begin();

        // ---------------- Cars (20 st) ----------------
        em.persist(new Car("Volkswagen", "Polo", 2022, CarType.SMALL, 399));
        em.persist(new Car("Toyota", "Yaris", 2023, CarType.SMALL, 429));
        em.persist(new Car("Hyundai", "i20", 2021, CarType.SMALL, 389));
        em.persist(new Car("Kia", "Rio", 2022, CarType.SMALL, 399));

        em.persist(new Car("Volvo", "V60", 2023, CarType.FAMILY, 699));
        em.persist(new Car("Volkswagen", "Passat", 2022, CarType.FAMILY, 649));
        em.persist(new Car("Skoda", "Octavia", 2023, CarType.FAMILY, 629));
        em.persist(new Car("Toyota", "Corolla Touring", 2022, CarType.FAMILY, 619));

        em.persist(new Car("Volkswagen", "Transporter", 2021, CarType.VAN, 899));
        em.persist(new Car("Ford", "Transit", 2022, CarType.VAN, 949));
        em.persist(new Car("Mercedes", "Vito", 2023, CarType.VAN, 999));
        em.persist(new Car("Renault", "Trafic", 2021, CarType.VAN, 879));

        em.persist(new Car("BMW", "M3", 2023, CarType.SPORT, 1999));
        em.persist(new Car("Audi", "RS5", 2022, CarType.SPORT, 1899));
        em.persist(new Car("Porsche", "911", 2023, CarType.SPORT, 2999));
        em.persist(new Car("Mercedes", "AMG GT", 2022, CarType.SPORT, 2799));

        em.persist(new Car("Mazda", "CX-5", 2022, CarType.FAMILY, 679));
        em.persist(new Car("Tesla", "Model Y", 2023, CarType.FAMILY, 799));
        em.persist(new Car("Mini", "Cooper", 2022, CarType.SMALL, 459));
        em.persist(new Car("Seat", "Leon", 2023, CarType.FAMILY, 599));

        // ---------------- Extras ----------------
        em.persist(new Extra("Försäkring", 149));
        em.persist(new Extra("Takbox", 99));
        em.persist(new Extra("GPS", 59));
        em.persist(new Extra("Barnstol", 79));

        em.getTransaction().commit();

        System.out.println(" Seed-data skapad (20 bilar + extras)");
    }
}
