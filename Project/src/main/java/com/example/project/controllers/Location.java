package com.example.project.controllers;

public class Location {
    // Aucun champ ne doit etre vide lors de l'ajout a la base de donnees
    private int ID; // fournit via une requete par la BD
    private String no_local;
    private String adresse;
    private int superficie;
    private int annee_construction; // doit varier de 1900 a l'annee courante (2023)

    public Location(){}

    public void setNoLocal(String no_local){
        if(no_local == "") throw new IllegalArgumentException("Vous devez specifie un numero de local.");
        this.no_local = no_local;
    }

    public void setAdresse(String adresse){
        if(no_local == "") throw new IllegalArgumentException("Vous devez specifie une adresse.");
        this.adresse = adresse;
    }

    public void setSuperficie(int superficie){
        if(superficie < 10) throw new IllegalArgumentException("La superficie doit etre d'au moins 10 metres carre");
        this.superficie = superficie;
    }

    public void setAnneeConstruction(int annee_construction){
        if(annee_construction < 1990 || annee_construction > 2023) throw new IllegalArgumentException("L'annee de construction doit varier de 1990 a 2023");
        this.annee_construction = annee_construction;
    }

}
