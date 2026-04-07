package com.likeherotozero.repository;

import com.likeherotozero.model.EmissionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstests fuer das EmissionDataRepository.
 *
 * Die Annotation @DataJpaTest konfiguriert automatisch eine eingebettete
 * H2-Datenbank und initialisiert nur die JPA-relevanten Beans. Dadurch
 * werden die Repository-Methoden gegen eine echte (In-Memory-)Datenbank getestet.
 */
@DataJpaTest
@ActiveProfiles("test")
class EmissionDataRepositoryTest {

    @Autowired
    private EmissionDataRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(new EmissionData("Deutschland", 2022, 8.1));
        repository.save(new EmissionData("Australien", 2022, 15.0));
        repository.save(new EmissionData("Indien", 2022, 1.9));
        repository.save(new EmissionData("Frankreich", 2022, 4.7));
        repository.save(new EmissionData("Vereinigte Staaten", 2022, 14.9));
    }

    @Test
    @DisplayName("findAll gibt alle gespeicherten Datensaetze zurueck")
    void findAll_returnsAllEntries() {
        List<EmissionData> result = repository.findAll();

        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("save speichert einen neuen Datensatz mit generierter ID")
    void save_persistsNewEntry() {
        EmissionData newEntry = new EmissionData("Japan", 2022, 8.5);

        EmissionData saved = repository.save(newEntry);

        assertNotNull(saved.getId(), "Die ID sollte nach dem Speichern generiert sein");
        assertEquals("Japan", saved.getCountry());
        assertEquals(6, repository.count());
    }

    @Test
    @DisplayName("findAllByOrderByCountryAsc sortiert alphabetisch nach Land")
    void findAllByOrderByCountryAsc_sortsByCountry() {
        List<EmissionData> result = repository.findAllByOrderByCountryAsc();

        assertEquals(5, result.size());
        assertEquals("Australien", result.get(0).getCountry());
        assertEquals("Deutschland", result.get(1).getCountry());
        assertEquals("Frankreich", result.get(2).getCountry());
        assertEquals("Indien", result.get(3).getCountry());
        assertEquals("Vereinigte Staaten", result.get(4).getCountry());
    }

    @Test
    @DisplayName("findAllByOrderByEmissionsPerCapitaDesc sortiert absteigend nach Emissionen")
    void findAllByOrderByEmissionsPerCapitaDesc_sortsByEmissionsDesc() {
        List<EmissionData> result = repository.findAllByOrderByEmissionsPerCapitaDesc();

        assertEquals(5, result.size());
        assertEquals("Australien", result.get(0).getCountry());       // 15.0
        assertEquals("Vereinigte Staaten", result.get(1).getCountry()); // 14.9
        assertEquals("Deutschland", result.get(2).getCountry());       // 8.1
        assertEquals("Frankreich", result.get(3).getCountry());        // 4.7
        assertEquals("Indien", result.get(4).getCountry());            // 1.9
    }

    @Test
    @DisplayName("findAllByOrderByEmissionsPerCapitaAsc sortiert aufsteigend nach Emissionen")
    void findAllByOrderByEmissionsPerCapitaAsc_sortsByEmissionsAsc() {
        List<EmissionData> result = repository.findAllByOrderByEmissionsPerCapitaAsc();

        assertEquals(5, result.size());
        assertEquals("Indien", result.get(0).getCountry());            // 1.9
        assertEquals("Australien", result.get(4).getCountry());       // 15.0
    }

    @Test
    @DisplayName("findByCountryIgnoreCase findet Datensaetze unabhaengig von Gross-/Kleinschreibung")
    void findByCountryIgnoreCase_isCaseInsensitive() {
        List<EmissionData> result = repository.findByCountryIgnoreCase("deutschland");

        assertEquals(1, result.size());
        assertEquals("Deutschland", result.get(0).getCountry());
    }

    @Test
    @DisplayName("findByYear gibt alle Datensaetze fuer ein Jahr zurueck")
    void findByYear_returnsMatchingEntries() {
        List<EmissionData> result = repository.findByYear(2022);

        assertEquals(5, result.size());
    }

    @Test
    @DisplayName("findByYear gibt leere Liste fuer nicht vorhandenes Jahr zurueck")
    void findByYear_returnsEmptyForMissingYear() {
        List<EmissionData> result = repository.findByYear(1999);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deleteById entfernt den Datensatz aus der Datenbank")
    void deleteById_removesEntry() {
        EmissionData saved = repository.save(new EmissionData("Brasilien", 2022, 2.3));
        Long id = saved.getId();

        repository.deleteById(id);

        assertFalse(repository.findById(id).isPresent());
    }
}
