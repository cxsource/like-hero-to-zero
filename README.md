# Like Hero To Zero

Webanwendung zur Darstellung und Verwaltung weltweiter CO2-Emissionsdaten pro Kopf nach Ländern.

**Fallstudie** im Modul *Programmierung von industriellen Informationssystemen mit Java EE* (IPWA02-01) an der IU Internationalen Hochschule.

## Technologiestack

- **Backend:** Spring Boot 3.4.4, Java 17
- **Sicherheit:** Spring Security 6 (rollenbasierte Zugriffskontrolle, BCrypt, CSRF-Schutz)
- **Frontend:** Thymeleaf 3 mit thymeleaf-extras-springsecurity6
- **Persistenz:** Spring Data JPA mit Hibernate (ORM-Provider)
- **Datenbank:** MySQL (Produktion) / H2 In-Memory (Entwicklung & Tests)
- **Build:** Maven 3.8+

## Voraussetzungen

- Java 17 oder hoeher
- Maven 3.8+
- MySQL 8.0 (optional — H2 ist als Standard vorkonfiguriert)

## Schnellstart (mit H2-Datenbank)

```bash
mvn spring-boot:run
```

Die Anwendung startet unter [http://localhost:8080](http://localhost:8080) und laedt automatisch Beispieldaten.

## Zugangsdaten (Entwicklung)

| Bereich | URL | Login erforderlich |
|---------|-----|--------------------|
| Öffentliche Übersicht | http://localhost:8080/emissions | Nein |
| Login-Seite | http://localhost:8080/login | — |
| Wissenschaftler-Backend | http://localhost:8080/backend | Ja (siehe unten) |
| H2-Konsole | http://localhost:8080/h2-console | Nein |

**Scientist-Login (Entwicklung):**
- Benutzername: `scientist`
- Passwort: `password123`

## Anwendung starten und pruefen

### Schritt 1: Projekt bauen

```bash
cd like-hero-to-zero
mvn clean install
```

### Schritt 2: Anwendung starten

```bash
mvn spring-boot:run
```

### Schritt 3: Funktionalitaet pruefen

1. **Öffentliche Ansicht:** [http://localhost:8080/emissions](http://localhost:8080/emissions)
   - Zeigt alle CO2-Emissionsdaten in einer Tabelle (ohne Login)
   - Sortierung nach Land (A-Z) oder Emissionswert moeglich

2. **Login als Wissenschaftler:** [http://localhost:8080/login](http://localhost:8080/login)
   - Benutzername: `scientist`, Passwort: `password123`
   - Nach Login Weiterleitung zum Backend-Dashboard

3. **Backend-Dashboard:** [http://localhost:8080/backend](http://localhost:8080/backend)
   - Vollstaendige CRUD-Verwaltung aller Datensaetze
   - Bearbeiten und Loeschen einzelner Eintraege

4. **Neue Daten eintragen:** [http://localhost:8080/backend/new](http://localhost:8080/backend/new)
   - Formular fuer Land, Jahr und CO2-Emissionen pro Kopf
   - Mit Bean Validation und CSRF-Schutz

5. **Logout:** Button im Backend-Header oder POST an `/logout`

### Konfiguration mit MySQL (Produktion)

1. Datenbank anlegen:
   ```sql
   CREATE DATABASE likeherotozerodb;
   ```

2. In `src/main/resources/application.properties` anpassen:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/likeherotozerodb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=IhrPasswort
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   # H2-Zeilen auskommentieren
   ```

## Tests ausfuehren

### Alle Tests

```bash
mvn test
```

### Einzelne Testklassen

```bash
mvn test -Dtest=EmissionDataControllerTest    # Oeffentliche Controller-Endpunkte
mvn test -Dtest=BackendControllerTest         # Geschuetzte Backend-Endpunkte
mvn test -Dtest=EmissionDataServiceTest       # Service-Schicht (Mockito)
mvn test -Dtest=EmissionDataRepositoryTest    # Repository / Datenbankzugriff
mvn test -Dtest=EmissionDataTest              # Entity-Validierung
```

### Testuebersicht

| Testklasse | Typ | Tests | Was wird getestet? |
|---|---|---|---|
| `EmissionDataControllerTest` | Web-Test (MockMvc) | 10 | GET /emissions, Sortierung, Status-Codes |
| `BackendControllerTest` | Web-Test (MockMvc) | 9 | GET /backend, POST /save, Redirect-Verhalten |
| `EmissionDataServiceTest` | Unit-Test (Mockito) | 6 | Geschaeftslogik, Delegation an Repository |
| `EmissionDataRepositoryTest` | Integrationstest (H2) | 8 | JPA-Abfragen, CRUD gegen In-Memory-DB |
| `EmissionDataTest` | Unit-Test | 10 | Bean Validation (@NotBlank, @Min, @Max) |

Gesamt: **43 Tests** ueber alle Schichten der Anwendung.

## Projektstruktur

```
src/main/java/com/likeherotozero/
├── LikeHeroToZeroApplication.java        # Hauptklasse (Einstiegspunkt)
├── config/
│   ├── DataInitializer.java              # Beispieldaten beim Start laden
│   └── SecurityConfig.java              # Spring Security Konfiguration
├── model/
│   └── EmissionData.java                # JPA-Entitaet (@Entity)
├── repository/
│   └── EmissionDataRepository.java      # Spring Data JPA Repository
├── service/
│   ├── IEmissionDataService.java        # Service-Interface (fuer Mockito)
│   └── EmissionDataService.java         # Geschaeftslogik (@Service)
└── controller/
    ├── EmissionDataController.java      # Oeffentliche Routen (/)
    └── BackendController.java           # Geschuetzte Routen (/backend/**)

src/main/resources/
├── application.properties              # Konfiguration (H2 Standard)
└── templates/
    ├── emissions.html                  # Oeffentliche Uebersichtsseite
    ├── login.html                      # Spring Security Login-Formular
    ├── backend.html                    # Wissenschaftler-Dashboard
    └── form.html                       # Dateneingabe-Formular

src/test/java/com/likeherotozero/
├── controller/
│   ├── EmissionDataControllerTest.java  # MockMvc Tests (oeffentlich)
│   └── BackendControllerTest.java       # MockMvc Tests (geschuetzt)
├── service/
│   └── EmissionDataServiceTest.java     # Service Unit-Tests (Mockito)
├── repository/
│   └── EmissionDataRepositoryTest.java  # Repository Integrationstests
└── model/
    └── EmissionDataTest.java            # Entity Unit-Tests
```

## User Stories (MoSCoW)

| ID | Beschreibung | Prioritaet | Status |
|---|---|---|---|
| US-1 | Als Bürger:in will ich CO2-Emissionsdaten meines Landes ohne Login einsehen können. | MUST | Umgesetzt |
| US-2 | Als registrierte:r Wissenschaftler:in will ich neue Klimaforschungsdaten eintragen können. | MUST | Umgesetzt |
| US-3 | Als Herausgeber:in will ich sicherstellen, dass Änderungen erst freigegeben werden müssen. | COULD | Nicht umgesetzt (Future Work) |

## Sicherheitsarchitektur

- **Öffentliche Routen:** `/`, `/emissions`, `/emissions/**`, `/login` — ohne Authentifizierung
- **Geschützte Routen:** `/backend/**` — erfordert Rolle `SCIENTIST`
- **Passwortverschlüsselung:** BCryptPasswordEncoder
- **CSRF-Schutz:** Spring Security (automatisch auf alle POST/PUT/DELETE-Anfragen)
- **Session-Management:** Spring Security Standard (HttpOnly + SecureFlag Cookies)

## Lizenz

Dieses Projekt wurde im Rahmen einer akademischen Fallstudie erstellt.
