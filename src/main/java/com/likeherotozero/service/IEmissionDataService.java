package com.likeherotozero.service;

import com.likeherotozero.model.EmissionData;

import java.util.List;
import java.util.Optional;

/**
 * Interface fuer die Geschaeftslogik der Emissionsdatenverwaltung.
 *
 * Dieses Interface definiert den Vertrag fuer die Service-Schicht und
 * ermoeglicht eine lose Kopplung zwischen Controller und Service.
 * Durch die Programmierung gegen Interfaces koennen verschiedene
 * Implementierungen (z.B. fuer Tests) problemlos ausgetauscht werden.
 */
public interface IEmissionDataService {

    List<EmissionData> getAllEmissions();

    List<EmissionData> getEmissionsSortedByCountry();

    List<EmissionData> getEmissionsSortedByEmissionsDesc();

    List<EmissionData> getEmissionsSortedByEmissionsAsc();

    Optional<EmissionData> getEmissionById(Long id);

    EmissionData saveEmission(EmissionData emissionData);

    void deleteEmission(Long id);
}
