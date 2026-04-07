package com.likeherotozero.service;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.repository.EmissionDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-Tests fuer die Service-Klasse EmissionDataService.
 *
 * Die Tests verwenden Mockito, um das Repository zu mocken. Dadurch werden
 * die Service-Methoden isoliert von der Datenbank getestet. Dies stellt
 * sicher, dass die Geschaeftslogik korrekt arbeitet, unabhaengig von der
 * konkreten Datenbankimplementierung.
 */
@ExtendWith(MockitoExtension.class)
class EmissionDataServiceTest {

    @Mock
    private EmissionDataRepository emissionDataRepository;

    @InjectMocks
    private EmissionDataService emissionDataService;

    private EmissionData germany;
    private EmissionData usa;
    private EmissionData india;

    @BeforeEach
    void setUp() {
        germany = new EmissionData("Deutschland", 2022, 8.1);
        germany.setId(1L);

        usa = new EmissionData("Vereinigte Staaten", 2022, 14.9);
        usa.setId(2L);

        india = new EmissionData("Indien", 2022, 1.9);
        india.setId(3L);
    }

    @Test
    @DisplayName("getAllEmissions ruft findAll auf und gibt alle Datensaetze zurueck")
    void getAllEmissions_returnsAllEntries() {
        when(emissionDataRepository.findAll())
                .thenReturn(Arrays.asList(germany, usa, india));

        List<EmissionData> result = emissionDataService.getAllEmissions();

        assertEquals(3, result.size());
        verify(emissionDataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getEmissionsSortedByCountry ruft findAllByOrderByCountryAsc auf")
    void getEmissionsSortedByCountry_callsCorrectRepositoryMethod() {
        when(emissionDataRepository.findAllByOrderByCountryAsc())
                .thenReturn(Arrays.asList(germany, india, usa));

        List<EmissionData> result = emissionDataService.getEmissionsSortedByCountry();

        assertEquals(3, result.size());
        assertEquals("Deutschland", result.get(0).getCountry());
        verify(emissionDataRepository, times(1)).findAllByOrderByCountryAsc();
    }

    @Test
    @DisplayName("getEmissionsSortedByEmissionsDesc ruft korrekte Repository-Methode auf")
    void getEmissionsSortedByEmissionsDesc_callsCorrectRepositoryMethod() {
        when(emissionDataRepository.findAllByOrderByEmissionsPerCapitaDesc())
                .thenReturn(Arrays.asList(usa, germany, india));

        List<EmissionData> result = emissionDataService.getEmissionsSortedByEmissionsDesc();

        assertEquals(3, result.size());
        assertEquals("Vereinigte Staaten", result.get(0).getCountry());
        verify(emissionDataRepository, times(1)).findAllByOrderByEmissionsPerCapitaDesc();
    }

    @Test
    @DisplayName("getEmissionById gibt Optional mit Datensatz zurueck")
    void getEmissionById_returnsOptionalWithData() {
        when(emissionDataRepository.findById(1L)).thenReturn(Optional.of(germany));

        Optional<EmissionData> result = emissionDataService.getEmissionById(1L);

        assertTrue(result.isPresent());
        assertEquals("Deutschland", result.get().getCountry());
    }

    @Test
    @DisplayName("getEmissionById gibt leeres Optional bei unbekannter ID zurueck")
    void getEmissionById_returnsEmptyForUnknownId() {
        when(emissionDataRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<EmissionData> result = emissionDataService.getEmissionById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("saveEmission speichert Datensatz und gibt gespeichertes Objekt zurueck")
    void saveEmission_persistsAndReturnsData() {
        EmissionData newData = new EmissionData("Japan", 2022, 8.5);
        EmissionData savedData = new EmissionData("Japan", 2022, 8.5);
        savedData.setId(4L);

        when(emissionDataRepository.save(any(EmissionData.class))).thenReturn(savedData);

        EmissionData result = emissionDataService.saveEmission(newData);

        assertNotNull(result.getId());
        assertEquals("Japan", result.getCountry());
        verify(emissionDataRepository, times(1)).save(newData);
    }

    @Test
    @DisplayName("deleteEmission ruft deleteById am Repository auf")
    void deleteEmission_callsDeleteById() {
        doNothing().when(emissionDataRepository).deleteById(1L);

        emissionDataService.deleteEmission(1L);

        verify(emissionDataRepository, times(1)).deleteById(1L);
    }
}
