package com.example.project.controllers;

public class Location {
    // Aucun champ ne doit etre vide lors de l'ajout a la base de donnees
    private int  ID; // fournit via une requete par la BD
    private String  no_local;
    private String  adresse;
    private int  superficie;
    private int  annee_construction; // doit varier de 1900 a l'annee courante (2023)

    public Location(){}

    public Location(int ID, String no_local, String adresse, int superficie, int annee_construction){
        this.ID = ID;
        this.no_local = no_local;
        this.adresse = adresse;
        this.superficie = superficie;
        this.annee_construction = annee_construction;
    }

    // Mutateurs (ils vont generer des messages d'erreur qui seront utilises pour avertir l'utilisateur via des boites d'alerte) :
    public void setID(String inputID){
        try {
            this.ID = Integer.parseInt(inputID);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le champ ID est vide.");
        }
    }

    public void setNoLocal(String no_local){
        if(no_local.isEmpty()) throw new IllegalArgumentException("Vous devez specifie un numero de local.");
        this.no_local = no_local;
    }

    public void setAdresse(String adresse){
        if(adresse.isEmpty()) throw new IllegalArgumentException("Vous devez specifier une adresse.");
        this.adresse = adresse;
    }

    public void setSuperficie(String inputSuperficie) {
        try {
            int superficie = Integer.parseInt(inputSuperficie);
            if (superficie < 10) {
                throw new IllegalArgumentException("La superficie doit être d'au moins 10 mètres carrés.");
            }
            this.superficie = superficie;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La valeur de la superficie doit être un nombre entier.");
        }
    }

    public void setAnneeConstruction(String inputAnneeConstruction) {
        try {
            int annee_construction = Integer.parseInt(inputAnneeConstruction);
            if(annee_construction < 1990 || annee_construction > 2023) throw new IllegalArgumentException("L'annee de construction doit varier de 1990 a 2023.");
            this.annee_construction = annee_construction;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'annee de construction doit être comprise entre 1990 et 2023.");
        }
    }

    // Accesseurs (ils doivent avec exactement la bonne syntaxe - get+nom de la variable - pour etre reconnus par la methode setCellValueFactory) :
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

    public int getAnnee_construction() {
        return annee_construction;
    }

    // Methode pour verification des resultats des requetes a la base de donnees et des creations de nouveaux objets Location.
    @Override
    public String toString(){
        return "ID: " + ID +", no_local: " + no_local +", adresse: " + adresse +", superficie: " + superficie +", annee_construction: " + annee_construction;
    }
}
