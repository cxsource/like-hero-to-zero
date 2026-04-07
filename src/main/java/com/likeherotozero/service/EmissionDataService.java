package com.likeherotozero.service;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.repository.EmissionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse fuer die Geschaeftslogik der Emissionsdatenverwaltung.
 *
 * Diese Klasse kapselt die Anwendungslogik und stellt eine Abstraktionsschicht
 * zwischen Controller und Repository dar. Sie ist mit {@code @Service} annotiert,
 * wodurch Spring sie als Bean erkennt und per Dependency Injection bereitstellt.
 *
 * Die Annotation {@code @Transactional} stellt sicher, dass Datenbankoperationen
 * innerhalb einer Transaktion ausgefuehrt werden (AKID-Prinzipien).
 */
@Service
@Transactional
public class EmissionDataService implements IEmissionDataService {

    private final EmissionDataRepository emissionDataRepository;

    /**
     * Konstruktor-basierte Dependency Injection des Repositories.
     *
     * @param emissionDataRepository das Repository fuer den Datenbankzugriff
     */
    @Autowired
    public EmissionDataService(EmissionDataRepository emissionDataRepository) {
        this.emissionDataRepository = emissionDataRepository;
    }

    /**
     * Gibt alle gespeicherten Emissionsdaten zurueck (unsortiert).
     * Umsetzung der User Story US-1.
     *
     * @return Liste aller EmissionData-Datensaetze
     */
    @Transactional(readOnly = true)
    public List<EmissionData> getAllEmissions() {
        return emissionDataRepository.findAll();
    }

    /**
     * Gibt alle Emissionsdaten alphabetisch nach Land sortiert zurueck.
     * Umsetzung der User Story US-3.
     *
     * @return Liste aller EmissionData, sortiert nach Land (A-Z)
     */
    @Transactional(readOnly = true)
    public List<EmissionData> getEmissionsSortedByCountry() {
        return emissionDataRepository.findAllByOrderByCountryAsc();
    }

    /**
     * Gibt alle Emissionsdaten absteigend nach Emissionswert sortiert zurueck.
     * Umsetzung der User Story US-3.
     *
     * @return Liste aller EmissionData, sortiert nach Emissionswert (absteigend)
     */
    @Transactional(readOnly = true)
    public List<EmissionData> getEmissionsSortedByEmissionsDesc() {
        return emissionDataRepository.findAllByOrderByEmissionsPerCapitaDesc();
    }

    /**
     * Gibt alle Emissionsdaten aufsteigend nach Emissionswert sortiert zurueck.
     *
     * @return Liste aller EmissionData, sortiert nach Emissionswert (aufsteigend)
     */
    @Transactional(readOnly = true)
    public List<EmissionData> getEmissionsSortedByEmissionsAsc() {
        return emissionDataRepository.findAllByOrderByEmissionsPerCapitaAsc();
    }

    /**
     * Sucht einen Emissionsdatensatz anhand seiner ID.
     *
     * @param id die ID des gesuchten Datensatzes
     * @return Optional mit dem Datensatz, falls vorhanden
     */
    @Transactional(readOnly = true)
    public Optional<EmissionData> getEmissionById(Long id) {
        return emissionDataRepository.findById(id);
    }

    /**
     * Speichert einen neuen oder aktualisierten Emissionsdatensatz.
     * Spring Data JPA entscheidet anhand des Primaerschluesselwerts
     * automatisch zwischen persist (neu) und merge (Update).
     * Umsetzung der User Story US-2.
     *
     * @param emissionData der zu speichernde Datensatz
     * @return der gespeicherte Datensatz (inkl. generierter ID)
     */
    public EmissionData saveEmission(EmissionData emissionData) {
        return emissionDataRepository.save(emissionData);
    }

    /**
     * Loescht einen Emissionsdatensatz anhand seiner ID.
     *
     * @param id die ID des zu loeschenden Datensatzes
     */
    public void deleteEmission(Long id) {
        emissionDataRepository.deleteById(id);
    }
}
