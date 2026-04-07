package com.likeherotozero.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests fuer die Entity-Klasse EmissionData.
 *
 * Getestet werden die Bean-Validation-Annotationen (@NotBlank, @NotNull, @Min, @Max etc.)
 * sowie die korrekte Funktionsweise der Getter und Setter.
 */
class EmissionDataTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Gueltige EmissionData erzeugt keine Validierungsfehler")
    void validEmissionData_noViolations() {
        EmissionData data = new EmissionData("Deutschland", 2022, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertTrue(violations.isEmpty(),
                "Ein gueltiger Datensatz sollte keine Validierungsfehler haben");
    }

    @Test
    @DisplayName("Leerer Laendername erzeugt Validierungsfehler")
    void emptyCountry_shouldFailValidation() {
        EmissionData data = new EmissionData("", 2022, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty(),
                "Ein leerer Laendername sollte einen Validierungsfehler erzeugen");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("country")));
    }

    @Test
    @DisplayName("Null-Laendername erzeugt Validierungsfehler")
    void nullCountry_shouldFailValidation() {
        EmissionData data = new EmissionData(null, 2022, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Jahr unter 1900 erzeugt Validierungsfehler")
    void yearTooLow_shouldFailValidation() {
        EmissionData data = new EmissionData("Deutschland", 1800, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty(),
                "Ein Jahr vor 1900 sollte einen Validierungsfehler erzeugen");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("year")));
    }

    @Test
    @DisplayName("Jahr über 2100 erzeugt Validierungsfehler")
    void yearTooHigh_shouldFailValidation() {
        EmissionData data = new EmissionData("Deutschland", 2200, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Negativer Emissionswert erzeugt Validierungsfehler")
    void negativeEmissions_shouldFailValidation() {
        EmissionData data = new EmissionData("Deutschland", 2022, -1.0);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty(),
                "Ein negativer Emissionswert sollte einen Validierungsfehler erzeugen");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("emissionsPerCapita")));
    }

    @Test
    @DisplayName("Null-Jahr erzeugt Validierungsfehler")
    void nullYear_shouldFailValidation() {
        EmissionData data = new EmissionData("Deutschland", null, 8.1);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Null-Emissionswert erzeugt Validierungsfehler")
    void nullEmissions_shouldFailValidation() {
        EmissionData data = new EmissionData("Deutschland", 2022, null);

        Set<ConstraintViolation<EmissionData>> violations = validator.validate(data);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Getter und Setter funktionieren korrekt")
    void gettersAndSetters_workCorrectly() {
        EmissionData data = new EmissionData();
        data.setId(1L);
        data.setCountry("Frankreich");
        data.setYear(2021);
        data.setEmissionsPerCapita(4.7);

        assertEquals(1L, data.getId());
        assertEquals("Frankreich", data.getCountry());
        assertEquals(2021, data.getYear());
        assertEquals(4.7, data.getEmissionsPerCapita());
    }

    @Test
    @DisplayName("Konstruktor mit Parametern setzt Werte korrekt")
    void parameterizedConstructor_setsValues() {
        EmissionData data = new EmissionData("Japan", 2022, 8.5);

        assertEquals("Japan", data.getCountry());
        assertEquals(2022, data.getYear());
        assertEquals(8.5, data.getEmissionsPerCapita());
        assertNull(data.getId(), "ID sollte vor dem Persistieren null sein");
    }

    @Test
    @DisplayName("toString gibt eine lesbare Darstellung zurueck")
    void toString_returnsReadableString() {
        EmissionData data = new EmissionData("China", 2022, 8.0);

        String result = data.toString();

        assertTrue(result.contains("China"));
        assertTrue(result.contains("2022"));
        assertTrue(result.contains("8.0"));
    }
}
