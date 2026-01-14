
USE car_rental;

INSERT INTO cars (brand, model, registrationNumber, pricePerHour, pricePerDay) VALUES
  ('Volvo', 'XC60', 'ABC 123', 120, 800),
  ('Tesla', 'Model 3', 'DEF 456', 150, 990),
  ('Toyota', 'Corolla', 'GHI 789', 90, 600);

INSERT INTO addons (name, price) VALUES
  ('Takbox', 150),
  ('Försäkring (Hel)', 200),
  ('Barnstol', 100),
  ('GPS', 50);
