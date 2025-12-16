-- ============================
-- DATABASE
-- ============================
CREATE DATABASE IF NOT EXISTS carrental;
USE carrental;

-- ============================
-- CUSTOMER
-- ============================
CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) UNIQUE NOT NULL
);

-- ============================
-- CAR
-- ============================
CREATE TABLE car (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     brand VARCHAR(100) NOT NULL,
                     model VARCHAR(100) NOT NULL,
                     price_per_day DOUBLE NOT NULL
);

-- ============================
-- BOOKING
-- ============================
CREATE TABLE booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         start_date DATE NOT NULL,
                         end_date DATE NOT NULL,
                         customer_id BIGINT NOT NULL,

                         CONSTRAINT fk_booking_customer
                             FOREIGN KEY (customer_id)
                                 REFERENCES customer(id)
                                 ON DELETE CASCADE
);

-- ============================
-- BOOKING_CAR (Many-to-Many)
-- ============================
CREATE TABLE booking_car (
                             booking_id BIGINT NOT NULL,
                             car_id BIGINT NOT NULL,

                             PRIMARY KEY (booking_id, car_id),

                             CONSTRAINT fk_bookingcar_booking
                                 FOREIGN KEY (booking_id)
                                     REFERENCES booking(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_bookingcar_car
                                 FOREIGN KEY (car_id)
                                     REFERENCES car(id)
                                     ON DELETE CASCADE
);

-- =================================================
-- ================== TESTDATA =====================
-- =================================================

-- ============================
-- CUSTOMERS
-- ============================
INSERT INTO customer (name, email) VALUES
                                       ('Eric Thilen', 'eric@test.se'),
                                       ('Torsten Wihlborg', 'torsten@test.se'),
                                       ('Viktor Lindell', 'viktor@test.se');

-- ============================
-- CARS (BILAR ATT HYRA)
-- ============================
INSERT INTO car (brand, model, price_per_day) VALUES
                                                  ('Volvo', 'XC60', 899),
                                                  ('Volvo', 'V90', 999),
                                                  ('BMW', 'X5', 1199),
                                                  ('Audi', 'A6', 1099),
                                                  ('Tesla', 'Model 3', 1299),
                                                  ('Toyota', 'Corolla', 699),
                                                  ('Honda', 'Civic', 599),
                                                  ('Skoda', 'Octavia', 499),
                                                  ('Seat', 'Leon', 399),
                                                  ('Seat', 'Ibiza', 299),
                                                  ('Fiat', 'Multipla', 399);


-- ============================
-- BOOKINGS
-- ============================
INSERT INTO booking (start_date, end_date, customer_id) VALUES
                                                            ('2025-01-10', '2025-01-15', 1),
                                                            ('2025-02-01', '2025-02-05', 2),
                                                            ('2025-03-20', '2025-03-25', 3);

-- ============================
-- BOOKING ↔ CAR
-- ============================
INSERT INTO booking_car (booking_id, car_id) VALUES
                                                 (1, 1),
                                                 (1, 2),
                                                 (2, 3),
                                                 (3, 5);
