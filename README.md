# Like Hero To Zero

Webanwendung zur Darstellung weltweiter CO2-Emissionsdaten pro Kopf nach Laendern.

**Fallstudie** im Modul *Programmierung von industriellen Informationssystemen mit Java EE* (IPWA02-01) an der IU Internationalen Hochschule.

## Technologiestack

- **Backend:** Spring Boot 3.2, Java 17
- **Frontend:** Thymeleaf (serverseitige Template-Engine)
- **Persistenz:** Spring Data JPA mit Hibernate (ORM-Provider)
- **Datenbank:** MySQL (Produktion) / H2 (Entwicklung)
- **Build:** Maven

## Voraussetzungen

- Java 17 oder hoeher
- Maven 3.8+
- MySQL 8.0 (optional, H2 ist als Standarddatenbank vorkonfiguriert)

## Schnellstart (mit H2-Datenbank)

Die Anwendung ist standardmaessig mit einer eingebetteten H2-Datenbank konfiguriert und kann ohne externe Datenbankinstallation gestartet werden:

```bash
mvn spring-boot:run
```

Die Anwendung ist unter [http://localhost:8080](http://localhost:8080) erreichbar.
Die H2-Konsole ist unter [http://localhost:8080/h2-console](http://localhost:8080/h2-console) verfuegbar.

## Konfiguration mit MySQL

1. MySQL-Datenbank anlegen:
   ```sql
   CREATE DATABASE likeherotozerodb;
   ```

2. In `src/main/resources/application.properties` die H2-Einstellungen auskommentieren und die MySQL-Einstellungen aktivieren:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/likeherotozerodb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=IhrPasswort
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```

3. Anwendung starten:
   ```bash
   mvn spring-boot:run
   ```

## Anwendung starten und pruefen

### Schritt 1: Projekt bauen

```bash
cd like-hero-to-zero
mvn clean install
```

Dieser Befehl kompiliert den Quellcode, fuehrt alle Tests aus und erstellt eine ausfuehrbare JAR-Datei unter `target/`.

### Schritt 2: Anwendung starten

```bash
mvn spring-boot:run
```

### Schritt 3: Im Browser oeffnen

- **Hauptseite:** [http://localhost:8080](http://localhost:8080) - zeigt die Emissionsdatentabelle (US-1)
- **Neue Daten:** [http://localhost:8080/emissions/new](http://localhost:8080/emissions/new) - Eingabeformular (US-2)
- **Sortiert nach Land:** [http://localhost:8080/emissions?sort=country](http://localhost:8080/emissions?sort=country) (US-3)
- **Sortiert nach Emissionen:** [http://localhost:8080/emissions?sort=emissionsDesc](http://localhost:8080/emissions?sort=emissionsDesc) (US-3)
- **H2-Konsole:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:likeherotozerodb`)

### Schritt 4: Funktionalitaet pruefen

1. Die Startseite zeigt 15 vorinstallierte Beispieldatensaetze in einer Tabelle
2. Klicke auf die Sortierlinks ueber der Tabelle, um die Daten nach Land oder Emissionswert zu sortieren
3. Klicke auf "Neue Emissionsdaten eintragen" und fuege einen neuen Datensatz hinzu (z.B. Land: "Mexiko", Jahr: 2022, Emissionen: 3.6)
4. Nach dem Speichern erscheint der neue Datensatz in der Tabelle
5. Teste die Validierung: sende das Formular mit leerem Land oder negativem Emissionswert ab - es sollten Fehlermeldungen erscheinen

## Tests ausfuehren

### Alle Tests

```bash
mvn test
```

### Einzelne Testklassen

```bash
mvn test -Dtest=EmissionDataTest                  # Entity-Validierung
mvn test -Dtest=EmissionDataRepositoryTest         # Repository / Datenbankzugriff
mvn test -Dtest=EmissionDataServiceTest            # Service / Geschaeftslogik
mvn test -Dtest=EmissionDataControllerTest         # Controller / HTTP-Endpunkte
mvn test -Dtest=LikeHeroToZeroApplicationTests     # Anwendungskontext
```

### Testuebersicht

| Testklasse | Typ | Anzahl | Was wird getestet? |
|-----------|-----|--------|-------------------|
| `EmissionDataTest` | Unit-Test | 10 | Bean-Validierung (@NotBlank, @Min, @Max), Getter/Setter |
| `EmissionDataRepositoryTest` | Integrationstest | 8 | JPA-Abfragen, Sortierung, CRUD gegen H2-Datenbank |
| `EmissionDataServiceTest` | Unit-Test (Mockito) | 6 | Geschaeftslogik, Delegation an Repository |
| `EmissionDataControllerTest` | Web-Test (MockMvc) | 10 | HTTP-Endpunkte, Validierung, Redirects |
| `LikeHeroToZeroApplicationTests` | Integrationstest | 1 | Anwendungskontext laed fehlerfrei |

Insgesamt: **35 Tests** ueber alle Schichten der Anwendung.

## Projektstruktur

```
src/main/java/com/likeherotozero/
├── LikeHeroToZeroApplication.java    # Hauptklasse (Einstiegspunkt)
├── config/
│   └── DataInitializer.java          # Beispieldaten beim Start laden
├── model/
│   └── EmissionData.java             # JPA-Entitaet (@Entity)
├── repository/
│   └── EmissionDataRepository.java   # Spring Data JPA Repository
├── service/
│   └── EmissionDataService.java      # Geschaeftslogik (@Service)
└── controller/
    └── EmissionDataController.java   # HTTP-Endpunkte (@Controller)

src/main/resources/
├── application.properties            # Konfiguration
└── templates/
    ├── emissions.html                # Uebersichtsseite (US-1, US-3)
    └── form.html                     # Eingabeformular (US-2)

src/test/java/com/likeherotozero/
├── LikeHeroToZeroApplicationTests.java           # Kontexttest
├── model/
│   └── EmissionDataTest.java                     # Entity Unit-Tests
├── repository/
│   └── EmissionDataRepositoryTest.java           # Repository Integrationstests
├── service/
│   └── EmissionDataServiceTest.java              # Service Unit-Tests (Mockito)
└── controller/
    └── EmissionDataControllerTest.java           # Controller Web-Tests (MockMvc)
```

## User Stories

| ID   | Beschreibung | Prioritaet | Status |
|------|-------------|-----------|--------|
| US-1 | Anzeige aller CO2-Emissionsdaten pro Kopf nach Laendern | MUST | Umgesetzt |
| US-2 | Eintragen neuer Emissionsdaten | MUST | Umgesetzt |
| US-3 | Sortierung der Daten nach Laendern / Emissionswerten | COULD | Umgesetzt |

## Lizenz

Dieses Projekt wurde im Rahmen einer akademischen Fallstudie erstellt.
