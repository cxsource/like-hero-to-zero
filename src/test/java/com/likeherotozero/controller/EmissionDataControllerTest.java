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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests fuer den oeffentlichen EmissionDataController mittels MockMvc.
 *
 * Dieser Controller bietet nur Lesezugriff (GET-Anfragen) auf die Emissionsdaten
 * und die Login-Seite. Die Verwaltung (POST, PUT, DELETE) erfolgt uber den
 * geschuetzten BackendController.
 *
 * Verwendet MockMvcBuilders.standaloneSetup() mit Mockito-Mocks,
 * um die HTTP-Endpunkte isoliert zu testen, ohne eine echte
 * Datenbank oder den vollstaendigen Spring-Kontext zu benoetigen.
 */
@ExtendWith(MockitoExtension.class)
class EmissionDataControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEmissionDataService emissionDataService;

    @InjectMocks
    private EmissionDataController emissionDataController;

    private List<EmissionData> testData;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(emissionDataController)
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

    // ==================== GET / ====================

    @Test
    @DisplayName("GET / leitet auf /emissions weiter")
    void index_redirectsToEmissions() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/emissions"));
    }

    // ==================== GET /emissions (US-1, public read-only) ====================

    @Test
    @DisplayName("GET /emissions zeigt oeffentliche Uebersichtsseite mit allen Daten (US-1)")
    void listEmissions_returnsEmissionsView() throws Exception {
        when(emissionDataService.getAllEmissions()).thenReturn(testData);

        mockMvc.perform(get("/emissions"))
                .andExpect(status().isOk())
                .andExpect(view().name("emissions"))
                .andExpect(model().attributeExists("emissions"))
                .andExpect(model().attribute("emissions", hasSize(3)));
    }

    @Test
    @DisplayName("GET /emissions?sort=country sortiert nach Land")
    void listEmissions_sortByCountry() throws Exception {
        when(emissionDataService.getEmissionsSortedByCountry()).thenReturn(testData);

        mockMvc.perform(get("/emissions").param("sort", "country"))
                .andExpect(status().isOk())
                .andExpect(view().name("emissions"))
                .andExpect(model().attribute("currentSort", "country"));
    }

    @Test
    @DisplayName("GET /emissions?sort=emissionsDesc sortiert absteigend nach Emissionen")
    void listEmissions_sortByEmissionsDesc() throws Exception {
        when(emissionDataService.getEmissionsSortedByEmissionsDesc()).thenReturn(testData);

        mockMvc.perform(get("/emissions").param("sort", "emissionsDesc"))
                .andExpect(status().isOk())
                .andExpect(view().name("emissions"));
    }

    @Test
    @DisplayName("GET /emissions?sort=emissionsAsc sortiert aufsteigend nach Emissionen")
    void listEmissions_sortByEmissionsAsc() throws Exception {
        when(emissionDataService.getEmissionsSortedByEmissionsAsc()).thenReturn(testData);

        mockMvc.perform(get("/emissions").param("sort", "emissionsAsc"))
                .andExpect(status().isOk())
                .andExpect(view().name("emissions"));
    }

    // ==================== GET /login ====================

    @Test
    @DisplayName("GET /login zeigt Login-Seite")
    void showLogin_returnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
