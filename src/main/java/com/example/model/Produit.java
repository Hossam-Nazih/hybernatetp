package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Produit {

    // Numéro de ligne en base, incrémenté tout seul à chaque insertion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Code article maison, doit être différent pour chaque produit
    @Column(unique = true)
    private String codeArticle;

    // Informations de présentation
    private String designation;
    private String descriptif;
    private String fabricant;
    private String rubrique;

    // Tarif et niveau de stock
    private BigDecimal tarif;
    private int stockDispo;

    // Masse de l'article en kg (sert à estimer l'expédition)
    private double masseKg;

    // Vrai tant que l'article est proposé à la vente
    private boolean enVente = true;

    // Jour où la fiche a été créée, rempli sans intervention
    private LocalDate creeLe;

    // Constructeur sans argument exigé par le moteur JPA
    public Produit() {
    }

    // Version courte : uniquement la désignation et le tarif
    public Produit(String designation, BigDecimal tarif) {
        this.designation = designation;
        this.tarif = tarif;
    }

    // Version détaillée avec les principales informations
    public Produit(String designation, BigDecimal tarif, String descriptif,
                   String fabricant, String rubrique, int stockDispo) {
        this.designation = designation;
        this.tarif = tarif;
        this.descriptif = descriptif;
        this.fabricant = fabricant;
        this.rubrique = rubrique;
        this.stockDispo = stockDispo;
    }

    // Fixe la date de création au moment du tout premier enregistrement
    @PrePersist
    private void initialiserDateCreation() {
        if (creeLe == null) {
            creeLe = LocalDate.now();
        }
    }

    // Vrai si l'article est en vente et qu'il en reste au moins un
    public boolean peutEtreVendu() {
        return enVente && stockDispo > 0;
    }

    // ----- Lecture et modification des champs -----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getTarif() {
        return tarif;
    }

    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getFabricant() {
        return fabricant;
    }

    public void setFabricant(String fabricant) {
        this.fabricant = fabricant;
    }

    public String getRubrique() {
        return rubrique;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }

    public int getStockDispo() {
        return stockDispo;
    }

    public void setStockDispo(int stockDispo) {
        this.stockDispo = stockDispo;
    }

    public double getMasseKg() {
        return masseKg;
    }

    public void setMasseKg(double masseKg) {
        this.masseKg = masseKg;
    }

    public boolean isEnVente() {
        return enVente;
    }

    public void setEnVente(boolean enVente) {
        this.enVente = enVente;
    }

    public LocalDate getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(LocalDate creeLe) {
        this.creeLe = creeLe;
    }

    // Représentation texte utilisée par println et les traces de debug
    @Override
    public String toString() {
        return "Produit[" +
                "id=" + id +
                " | code=" + codeArticle +
                " | " + designation +
                " | tarif=" + tarif +
                " | " + descriptif +
                " | fabricant=" + fabricant +
                " | rubrique=" + rubrique +
                " | stock=" + stockDispo +
                " | masse=" + masseKg + "kg" +
                " | enVente=" + enVente +
                " | créé le " + creeLe +
                ']';
    }
}