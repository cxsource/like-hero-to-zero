package com.likeherotozero.controller;

import com.likeherotozero.model.EmissionData;
import com.likeherotozero.service.IEmissionDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Backend-Controller fuer registrierte Wissenschaftler:innen.
 *
 * Dieser Controller erfordert eine Authentifizierung mit der Rolle SCIENTIST
 * und stellt die Backend-Oberflaeche zur Verwaltung der Emissionsdaten bereit.
 * Umsetzung der User Story US-2.
 */
@Controller
@RequestMapping("/backend")
public class BackendController {

    private final IEmissionDataService emissionDataService;

    @Autowired
    public BackendController(IEmissionDataService emissionDataService) {
        this.emissionDataService = emissionDataService;
    }

    /**
     * Backend-Uebersicht: Zeigt alle Emissionsdaten mit Verwaltungsoptionen.
     */
    @GetMapping
    public String dashboard(Model model) {
        List<EmissionData> emissions = emissionDataService.getAllEmissions();
        model.addAttribute("emissions", emissions);
        return "backend";
    }

    /**
     * Zeigt das Formular zum Eintragen neuer Emissionsdaten.
     */
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("emissionData", new EmissionData());
        return "form";
    }

    /**
     * Speichert neue oder aktualisierte Emissionsdaten.
     * Bei Validierungsfehlern wird das Formular erneut angezeigt.
     * Bei Erfolg wird per Post-Redirect-Get auf das Backend weitergeleitet.
     */
    @PostMapping("/save")
    public String saveEmission(@Valid @ModelAttribute("emissionData") EmissionData emissionData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        emissionDataService.saveEmission(emissionData);
        redirectAttributes.addFlashAttribute("successMessage",
                "Emissionsdaten fuer " + emissionData.getCountry() + " erfolgreich gespeichert.");
        return "redirect:/backend";
    }

    /**
     * Zeigt das Bearbeitungsformular fuer einen bestehenden Datensatz.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        EmissionData emissionData = emissionDataService.getEmissionById(id).orElse(null);
        if (emissionData == null) {
            return "redirect:/backend";
        }
        model.addAttribute("emissionData", emissionData);
        return "form";
    }

    /**
     * Loescht einen Emissionsdatensatz und leitet zurueck zum Backend.
     */
    @GetMapping("/delete/{id}")
    public String deleteEmission(@PathVariable("id") Long id,
                                  RedirectAttributes redirectAttributes) {
        emissionDataService.deleteEmission(id);
        redirectAttributes.addFlashAttribute("successMessage", "Datensatz erfolgreich geloescht.");
        return "redirect:/backend";
    }
}
