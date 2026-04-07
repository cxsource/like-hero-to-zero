package com.likeherotozero.controller;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.service.IEmissionDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests fuer den Backend-Controller mittels MockMvc.
 *
 * Dieser Controller bietet Verwaltungsfunktionen (Create, Read, Update, Delete)
 * fuer Emissionsdaten und erfordert Authentifizierung mit SCIENTIST-Rolle.
 *
 * Verwendet MockMvcBuilders.standaloneSetup() mit Mockito-Mocks,
 * um die HTTP-Endpunkte isoliert zu testen, ohne eine echte
 * Datenbank oder den vollstaendigen Spring-Kontext zu benoetigen.
 */
@ExtendWith(MockitoExtension.class)
class BackendControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEmissionDataService emissionDataService;

    @InjectMocks
    private BackendController backendController;

    private List<EmissionData> testData;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(backendController)
                .setViewResolvers(viewResolver)
                .build();

        EmissionData germany = new EmissionData("Deutschland", 2022, 8.1);
        germany.setId(1L);
        EmissionData usa = new EmissionData("Vereinigte Staaten", 2022, 14.9);
        usa.setId(2L);
        EmissionData india = new EmissionData("Indien", 2022, 1.9);
        india.setId(3L);

        testData = Arrays.asList(germany, usa, india);
    }

    // ==================== GET /backend ====================

    @Test
    @DisplayName("GET /backend zeigt Dashboard mit allen Emissionsdaten")
    void dashboard_returnsBackendView() throws Exception {
        when(emissionDataService.getAllEmissions()).thenReturn(testData);

        mockMvc.perform(get("/backend"))
                .andExpect(status().isOk())
                .andExpect(view().name("backend"))
                .andExpect(model().attributeExists("emissions"))
                .andExpect(model().attribute("emissions", hasSize(3)));

        verify(emissionDataService, times(1)).getAllEmissions();
    }

    // ==================== GET /backend/new ====================

    @Test
    @DisplayName("GET /backend/new zeigt leeres Formular fuer neue Daten")
    void showForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/backend/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeExists("emissionData"));
    }

    // ==================== POST /backend/save ====================

    @Test
    @DisplayName("POST /backend/save speichert gueltige Daten und leitet zum Backend weiter")
    void saveEmission_validData_redirectsToBackend() throws Exception {
        EmissionData saved = new EmissionData("Japan", 2022, 8.5);
        saved.setId(4L);
        when(emissionDataService.saveEmission(any(EmissionData.class))).thenReturn(saved);

        mockMvc.perform(post("/backend/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("country", "Japan")
                        .param("year", "2022")
                        .param("emissionsPerCapita", "8.5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/backend"));

        verify(emissionDataService, times(1)).saveEmission(any(EmissionData.class));
    }

    @Test
    @DisplayName("POST /backend/save mit leerem Land zeigt Formular erneut mit Fehler")
    void saveEmission_emptyCountry_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/backend/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("country", "")
                        .param("year", "2022")
                        .param("emissionsPerCapita", "8.5"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeHasFieldErrors("emissionData", "country"));

        verify(emissionDataService, never()).saveEmission(any());
    }

    @Test
    @DisplayName("POST /backend/save ohne Jahr zeigt Formular erneut mit Fehler")
    void saveEmission_nullYear_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/backend/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("country", "Japan")
                        .param("year", "")
                        .param("emissionsPerCapita", "8.5"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));

        verify(emissionDataService, never()).saveEmission(any());
    }

    @Test
    @DisplayName("POST /backend/save mit negativem Emissionswert zeigt Formular mit Fehler")
    void saveEmission_negativeEmissions_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/backend/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("country", "Japan")
                        .param("year", "2022")
                        .param("emissionsPerCapita", "-5.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));

        verify(emissionDataService, never()).saveEmission(any());
    }

    // ==================== GET /backend/edit/{id} ====================

    @Test
    @DisplayName("GET /backend/edit/{id} zeigt Formular mit vorhandenen Daten")
    void showEditForm_existingId_returnsFormWithData() throws Exception {
        EmissionData germany = testData.get(0);
        when(emissionDataService.getEmissionById(1L)).thenReturn(Optional.of(germany));

        mockMvc.perform(get("/backend/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attribute("emissionData", germany));
    }

    @Test
    @DisplayName("GET /backend/edit/{id} mit ungueltiger ID leitet zum Backend weiter")
    void showEditForm_invalidId_redirectsToBackend() throws Exception {
        when(emissionDataService.getEmissionById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/backend/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/backend"));
    }

    // ==================== GET /backend/delete/{id} ====================

    @Test
    @DisplayName("GET /backend/delete/{id} loescht Datensatz und leitet zum Backend weiter")
    void deleteEmission_redirectsToBackend() throws Exception {
        doNothing().when(emissionDataService).deleteEmission(1L);

        mockMvc.perform(get("/backend/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/backend"));

        verify(emissionDataService, times(1)).deleteEmission(1L);
    }
}
