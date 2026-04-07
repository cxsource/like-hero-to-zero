package com.likeherotozero.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * JPA-Entitaetsklasse fuer CO2-Emissionsdaten.
 *
 * Diese Klasse repraesentiert einen Datensatz mit laenderbezogenen
 * CO2-Emissionen pro Kopf fuer ein bestimmtes Jahr. Sie wird durch
 * die Annotation {@code @Entity} als persistierbare JPA-Entitaet
 * gekennzeichnet und auf die Datenbanktabelle "emission_data" abgebildet.
 *
 * Gemaess den JPA-Konventionen implementiert die Klasse einen parameterlosen
 * Konstruktor sowie Getter- und Setter-Methoden fuer alle Attribute.
 */
@Entity
@Table(name = "emission_data")
public class EmissionData {

    /**
     * Eindeutiger Primaerschluessel. Wird durch die Datenbank
     * automatisch generiert (Auto-Increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name des Landes. Pflichtfeld, darf nicht leer sein.
     */
    @NotBlank(message = "Der Laendername darf nicht leer sein.")
    @Size(max = 100, message = "Der Laendername darf maximal 100 Zeichen lang sein.")
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    /**
     * Bezugsjahr der Emissionsdaten.
     */
    @NotNull(message = "Das Jahr muss angegeben werden.")
    @Min(value = 1900, message = "Das Jahr muss mindestens 1900 sein.")
    @Max(value = 2100, message = "Das Jahr darf maximal 2100 sein.")
    @Column(name = "`year`", nullable = false)
    private Integer year;

    /**
     * CO2-Emissionen pro Kopf in Tonnen.
     */
    @NotNull(message = "Der Emissionswert muss angegeben werden.")
    @DecimalMin(value = "0.0", message = "Der Emissionswert darf nicht negativ sein.")
    @Column(name = "emissions_per_capita", nullable = false)
    private Double emissionsPerCapita;

    // ==================== Konstruktoren ====================

    /**
     * Parameterloser Standardkonstruktor (von JPA gefordert).
     */
    public EmissionData() {
    }

    /**
     * Konstruktor mit allen fachlichen Attributen.
     *
     * @param country           Name des Landes
     * @param year              Bezugsjahr
     * @param emissionsPerCapita CO2-Ausstoss pro Kopf in Tonnen
     */
    public EmissionData(String country, Integer year, Double emissionsPerCapita) {
        this.country = country;
        this.year = year;
        this.emissionsPerCapita = emissionsPerCapita;
    }

    // ==================== Getter und Setter ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getEmissionsPerCapita() {
        return emissionsPerCapita;
    }

    public void setEmissionsPerCapita(Double emissionsPerCapita) {
        this.emissionsPerCapita = emissionsPerCapita;
    }

    // ==================== toString ====================

    @Override
    public String toString() {
        return "EmissionData{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", year=" + year +
                ", emissionsPerCapita=" + emissionsPerCapita +
                '}';
    }
}
