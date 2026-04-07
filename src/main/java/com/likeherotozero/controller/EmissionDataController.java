package com.likeherotozero.controller;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.service.IEmissionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Oeffentlicher Controller fuer die Anzeige der CO2-Emissionsdaten.
 *
 * Dieser Controller ist ohne Authentifizierung zugaenglich und stellt
 * die oeffentliche Ansicht der Emissionsdaten bereit (User Story US-1).
 * Das Eintragen und Bearbeiten von Daten ist dem BackendController
 * vorbehalten, der eine Authentifizierung erfordert.
 */
@Controller
public class EmissionDataController {

    private final IEmissionDataService emissionDataService;

    @Autowired
    public EmissionDataController(IEmissionDataService emissionDataService) {
        this.emissionDataService = emissionDataService;
    }

    /**
     * Startseite - leitet auf die oeffentliche Emissionsuebersicht weiter.
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/emissions";
    }

    /**
     * Zeigt alle Emissionsdaten in tabellarischer Form an (oeffentlich, ohne Login).
     * Unterstuetzt optionale Sortierung ueber den Query-Parameter "sort".
     *
     * Umsetzung der User Story US-1.
     *
     * @param sort  optionaler Sortierparameter
     * @param model das Model-Objekt fuer die View
     * @return Name des Thymeleaf-Templates
     */
    @GetMapping("/emissions")
    public String listEmissions(@RequestParam(name = "sort", required = false) String sort,
                                 Model model) {
        List<EmissionData> emissions;

        if ("country".equals(sort)) {
            emissions = emissionDataService.getEmissionsSortedByCountry();
        } else if ("emissionsDesc".equals(sort)) {
            emissions = emissionDataService.getEmissionsSortedByEmissionsDesc();
        } else if ("emissionsAsc".equals(sort)) {
            emissions = emissionDataService.getEmissionsSortedByEmissionsAsc();
        } else {
            emissions = emissionDataService.getAllEmissions();
        }

        model.addAttribute("emissions", emissions);
        model.addAttribute("currentSort", sort);
        return "emissions";
    }

    /**
     * Zeigt die Login-Seite an.
     */
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
}
