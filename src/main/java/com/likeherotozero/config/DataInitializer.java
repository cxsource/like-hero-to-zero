package com.likeherotozero.config;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.repository.EmissionDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurationsklasse zum Befuellen der Datenbank mit Beispieldaten.
 *
 * Beim Start der Anwendung werden automatisch einige CO2-Emissionsdatensaetze
 * in die Datenbank eingefuegt, sofern diese noch leer ist. Dies dient der
 * Demonstration des Prototyps und erleichtert das Testen der Anwendung.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EmissionDataRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Beispieldaten: CO2-Emissionen pro Kopf in Tonnen (Quelle: Global Carbon Project)
                repository.save(new EmissionData("Deutschland", 2022, 8.1));
                repository.save(new EmissionData("Vereinigte Staaten", 2022, 14.9));
                repository.save(new EmissionData("China", 2022, 8.0));
                repository.save(new EmissionData("Indien", 2022, 1.9));
                repository.save(new EmissionData("Brasilien", 2022, 2.3));
                repository.save(new EmissionData("Japan", 2022, 8.5));
                repository.save(new EmissionData("Russland", 2022, 11.4));
                repository.save(new EmissionData("Kanada", 2022, 14.3));
                repository.save(new EmissionData("Frankreich", 2022, 4.7));
                repository.save(new EmissionData("Vereinigtes Königreich", 2022, 5.2));
                repository.save(new EmissionData("Australien", 2022, 15.0));
                repository.save(new EmissionData("Suedkorea", 2022, 11.6));
                repository.save(new EmissionData("Schweden", 2022, 3.6));
                repository.save(new EmissionData("Norwegen", 2022, 7.5));
                repository.save(new EmissionData("Schweiz", 2022, 4.0));

                System.out.println("=== Beispieldaten erfolgreich geladen ===");
            }
        };
    }
}
