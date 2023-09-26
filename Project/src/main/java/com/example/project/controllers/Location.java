package com.example.project.controllers;

public class Location {
    // Aucun champ ne doit etre vide lors de l'ajout a la base de donnees
    private int  ID; // fournit via une requete par la BD
    private String  no_local;
    private String  adresse;
    private int  superficie;
    private int  annee_construction; // doit varier de 1900 a l'annee courante (2023)

//    public Location(){}

    public Location(int ID, String no_local, String adresse, int superficie, int annee_construction){
        this.ID = ID;
        this.no_local = no_local;
        this.adresse = adresse;
        this.superficie = superficie;
        this.annee_construction = annee_construction;
    }

    public void setNoLocal(String no_local){
        if(no_local == "") throw new IllegalArgumentException("Vous devez specifie un numero de local.");
        this.no_local = no_local;
    }

    public void setAdresse(String adresse){
        if(adresse == "") throw new IllegalArgumentException("Vous devez specifie une adresse.");
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

    public int getID() {
        return ID;
    }

    public String getNo_local() {
        return no_local;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getSuperficie() {
        return superficie;
    }

    public int getAnneeConstruction() {
        return annee_construction;
    }

    @Override
    public String toString(){
        return "ID: " + ID +", no_local: " + no_local +", adresse: " + adresse +", superficie: " + superficie +", annee_construction: " + annee_construction;
    }
}
