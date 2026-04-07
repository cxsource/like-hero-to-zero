package com.likeherotozero.repository;

import com.likeherotozero.model.EmissionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository-Interface fuer den Datenbankzugriff auf EmissionData-Entitaeten.
 *
 * Durch die Erweiterung von {@link JpaRepository} erbt dieses Interface
 * grundlegende CRUD-Operationen (findAll, findById, save, deleteById etc.).
 * Spring Data JPA erzeugt zur Laufzeit automatisch eine Implementierung.
 *
 * Zusaetzlich werden eigene Abfragemethoden deklariert, deren JPQL-Queries
 * von Spring Data JPA automatisch aus den Methodennamen abgeleitet werden
 * (Query Derivation).
 */
@Repository
public interface EmissionDataRepository extends JpaRepository<EmissionData, Long> {

    /**
     * Gibt alle Emissionsdaten alphabetisch aufsteigend nach Laendernamen zurueck.
     * Umsetzung der User Story US-3 (Sortierung nach Land).
     *
     * @return Liste aller EmissionData, sortiert nach country ASC
     */
    List<EmissionData> findAllByOrderByCountryAsc();

    /**
     * Gibt alle Emissionsdaten absteigend nach Emissionswert zurueck.
     * Umsetzung der User Story US-3 (Sortierung nach Emissionswert).
     *
     * @return Liste aller EmissionData, sortiert nach emissionsPerCapita DESC
     */
    List<EmissionData> findAllByOrderByEmissionsPerCapitaDesc();

    /**
     * Gibt alle Emissionsdaten aufsteigend nach Emissionswert zurueck.
     *
     * @return Liste aller EmissionData, sortiert nach emissionsPerCapita ASC
     */
    List<EmissionData> findAllByOrderByEmissionsPerCapitaAsc();

    /**
     * Sucht Emissionsdaten fuer ein bestimmtes Land.
     *
     * @param country Name des Landes
     * @return Liste der EmissionData fuer das angegebene Land
     */
    List<EmissionData> findByCountryIgnoreCase(String country);

    /**
     * Sucht Emissionsdaten fuer ein bestimmtes Jahr.
     *
     * @param year Bezugsjahr
     * @return Liste der EmissionData fuer das angegebene Jahr
     */
    List<EmissionData> findByYear(Integer year);
}
