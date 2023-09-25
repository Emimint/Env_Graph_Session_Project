package com.example.project.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class Location {
    // Aucun champ ne doit etre vide lors de l'ajout a la base de donnees
    private IntegerProperty  ID; // fournit via une requete par la BD
    private StringProperty  no_local;
    private StringProperty  adresse;
    private IntegerProperty  superficie;
    private IntegerProperty  annee_construction; // doit varier de 1900 a l'annee courante (2023)

//    public Location(){}

    public Location(int ID, String no_local, String adresse, int superficie, int annee_construction){
        this.ID = new SimpleIntegerProperty(ID);
        this.no_local = new SimpleStringProperty(no_local);
        this.adresse = new SimpleStringProperty(adresse);
        this.superficie = new SimpleIntegerProperty(superficie);
        this.annee_construction = new SimpleIntegerProperty(annee_construction);
    }

    public void setNoLocal(String no_local){
        if(no_local == "") throw new IllegalArgumentException("Vous devez specifie un numero de local.");
        this.no_local = new SimpleStringProperty(no_local);
    }

    public void setAdresse(String adresse){
        if(adresse == "") throw new IllegalArgumentException("Vous devez specifie une adresse.");
        this.adresse = new SimpleStringProperty(adresse);
    }

    public void setSuperficie(int superficie){
        if(superficie < 10) throw new IllegalArgumentException("La superficie doit etre d'au moins 10 metres carre");
        this.superficie = new SimpleIntegerProperty(superficie);
    }

    public void setAnneeConstruction(int annee_construction){
        if(annee_construction < 1990 || annee_construction > 2023) throw new IllegalArgumentException("L'annee de construction doit varier de 1990 a 2023");
        this.annee_construction = new SimpleIntegerProperty(annee_construction);
    }

    public IntegerProperty getID() {
        return ID;
    }

    public StringProperty no_localProperty() {
        return no_local;
    }

    public StringProperty adresseProperty() {
        return adresse;
    }

    public IntegerProperty getSuperficie() {
        return superficie;
    }

    public IntegerProperty getAnneeConstruction() {
        return annee_construction;
    }
}
