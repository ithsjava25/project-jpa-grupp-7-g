[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/_uV8Mn8f)
# 📘 Projektarbete: JPA + Hibernate med GitHub-flöde

Projektet genomförs som antingen en Java CLI-applikation eller med hjälp av JavaFX om ni vill ha ett grafiskt gränssnitt.
Arbetet utförs i grupper om fyra deltagare. Ni bildar grupperna genom att antingen skapa en ny grupp eller
ansluta till en befintlig grupp via GitHub Classrooms.

Projektet ska använda en relationsdatabas, där MySQL eller PostgreSQL rekommenderas.
Kommunikation med databasen ska ske med JPA och Hibernate, enligt code first-metoden.

## 🗓️ Veckoplanering med Checklista
### ✅ Vecka 1 – Grundläggning och struktur
- [ ] Klona GitHub-repo
- [ ] Konfigurera persistence.xml eller använd PersistenceConfiguration i kod
- [ ] Skapa entiteter och verifiera tabellgenerering
- [ ] Lägg till relationer (One-to-Many, Many-to-Many)
- [ ] Arbeta på feature-branches och använd pull requests för kodgranskning

### ✅ Vecka 2 – Funktionalitet och relationer
- [ ] Dela upp funktioner mellan gruppmedlemmar
- [ ] Implementera funktionalitet för huvudentiteter
- [ ] Testa queries med EntityManager
- [ ] Dokumentera större designbeslut i PR-beskrivningar
- [ ] Säkerställ att alla merges sker via kodgranskning

### ✅ Vecka 3 – Finslipning och presentation
- [ ] Lägg till validering, felhantering och loggning
- [ ] Skriv enhetstester för centrala funktioner
- [ ] Förbered demo (~10 min):
- [ ] Visa applikationen (CLI-kommandon eller GUI)
- [ ] Gå igenom datamodellen och relationerna

## 🎯 Projektförslag
### Spelturnering / E-sportplattform 🎮

One-to-Many: En turnering → flera matcher

Many-to-Many: Spelare ↔ Lag

### Film- och serietjänst (à la Netflix) 🎬

One-to-Many: En regissör → flera filmer/serier

Many-to-Many: Användare ↔ Favoritlistor

### Musikspelare 🎵

One-to-Many: En artist → flera album

Many-to-Many: Album ↔ Spellistor

### Projekt- och uppgiftshantering 📋

One-to-Many: Ett projekt → flera uppgifter

Many-to-Many: Uppgifter ↔ Användare

### Restaurangbokning 🍽️

One-to-Many: En restaurang → flera bord

Many-to-Many: Gäster ↔ Bokningar


---

## MySQL i Docker för detta projekt

Följ dessa steg för att köra MySQL lokalt i Docker och koppla upp applikationen.

### 1) Starta MySQL 8 med Docker Compose

Kräver Docker Desktop (eller docker + docker compose plugin).

Kör i projektroten:
```
docker compose up -d
```
Det startar en MySQL 8-container med:
- Databas: `car_rental`
- Användare: `car_user`
- Lösenord: `strong_password_here`
- Port: `3306` (exponerad på localhost)

### 2) JPA/Hibernate är konfigurerat för MySQL
`src/main/resources/META-INF/persistence.xml` pekar nu på:
```
jdbc:mysql://localhost:3306/car_rental
user=car_user, password=strong_password_here
```
Hibernate skapar/uppdaterar tabellerna vid start (`hibernate.hbm2ddl.auto=update`).

### 3) Verifiera och fyll på data
- Verifiera att containern är igång:
```
docker ps
```
- Logga in (om du har MySQL-klient):
```
mysql -h 127.0.0.1 -P 3306 -u car_user -p
USE car_rental;
SHOW TABLES;
```
- Lägg in seed-data (efter att applikationen skapat tabellerna) med filen `sql/seeds.sql`:
```
mysql -h 127.0.0.1 -P 3306 -u car_user -p car_rental < sql/seeds.sql
```

### 4) Starta applikationen
- Bygg och kör som vanligt (t.ex. via IntelliJ eller Maven). Vid start kommer Hibernate att skapa tabellerna i MySQL.

### 5) Felsökning
- Port 3306 upptagen → stoppa annan MySQL eller ändra port i `docker-compose.yml` och i `persistence.xml`.
- Access denied → kontrollera user/lösen i både compose och `persistence.xml`.
- No suitable driver → kör `mvn clean package` och säkerställ att `mysql-connector-j` finns i `pom.xml`.
