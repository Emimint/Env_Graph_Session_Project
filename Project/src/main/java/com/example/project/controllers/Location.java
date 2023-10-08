package com.example.project.controllers;

import java.util.Calendar;

public class Location {
    // Aucun champ ne doit etre vide lors de l'ajout a la base de donnees
    private int  ID; // fournit via une requete par la BD
    private String  no_local;
    private String  adresse;
    private int  superficie;
    private int  annee_construction; // doit varier de 1900 a l'annee courante (2023)
    private boolean status;
    private boolean disponible;
    private int date_debut;
    private int date_fin;
    private int prix_pied_carre;

    int anneeCourante = Calendar.getInstance().get(Calendar.YEAR);

    public Location(){}

    public Location(int ID, String no_local, String adresse, int superficie, int annee_construction, boolean status, boolean disponible, int date_debut, int date_fin, int prix_pied_carre){
        this.ID = ID;
        this.no_local = no_local;
        this.adresse = adresse;
        this.superficie = superficie;
        this.annee_construction = annee_construction;
        this.status = status;
        this.disponible = disponible;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.prix_pied_carre = prix_pied_carre;
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
            if(annee_construction < 1900 || annee_construction > anneeCourante) throw new IllegalArgumentException("L'annee de construction doit varier de 1900 a 2023.");
            this.annee_construction = annee_construction;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("L'annee de construction doit être un entier positif.");
        }
    }

    public void setStatus(String inputStatus){
        if(date_debut == 0 || date_fin == 0) throw new IllegalArgumentException("Vous devez preciser la periode de location.");
        boolean status = Boolean.parseBoolean(inputStatus);
        if (!disponible) throw new IllegalArgumentException("Cette location n'est pas disponible pour le moment.");
        this.status = status;
    }

    public void setDisponible(String inputDisponible){
        this.disponible = Boolean.parseBoolean(inputDisponible);
        if(!disponible) date_fin = Math.min(date_fin, anneeCourante);
    }

    public void setDate_debut(String inputDate_debut){
        try {
            int date_debut = Integer.parseInt(inputDate_debut);
            if(!((date_debut >= 1900 && date_debut <= anneeCourante) && (date_debut >= annee_construction)))
                throw new IllegalArgumentException("" +
                    "\n- L'annee de location doit varier de 1900 a 2023." +
                    "\n- l'annee de location ne peut pas etre plus ancienne que l'annee de construction.");
            this.date_debut = date_debut;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("L'annee de location doit être un entier positif.");
        }
    }

    public void setDate_fin (String inputDate_fin){
        try {
            int date_fin = Integer.parseInt(inputDate_fin);
            if(!((date_debut >= 1900 && date_debut <= anneeCourante) && (date_fin > date_debut)))
                throw new IllegalArgumentException("" +
                        "\n- L'annee de fin de location doit varier de 1900 a 2023." +
                        "\n- l'annee de fin de location ne peut pas etre plus recente que l'annee de debut de location.");
            this.date_fin = date_fin;
            if(this.date_fin >= anneeCourante) setStatus("True");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("L'annee de fin de location doit être un entier positif.");
        }
    }

    public void setPrix_pied_carre(String inputPrix_pied_carre){
        try {
            int prix_pied_carre = Integer.parseInt(inputPrix_pied_carre);
            if((!(prix_pied_carre <= 400 && prix_pied_carre >= 20))) throw new IllegalArgumentException("Le prix au pied carre doit etre compris entre 20 et 400$.");
            this.prix_pied_carre = prix_pied_carre;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit etre un entier positif.");
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

    public boolean getStatus(){
        return status;
    }

    public boolean getDisponible(){
    return disponible;
    }

    public int getDate_debut(){
        return date_debut;
    }

    public int getDate_fin(){
        return date_fin;
    }

    public int getPrix_pied_carre(){
        return prix_pied_carre;
    }

    // Methode pour verification des resultats des requetes a la base de donnees et des creations de nouveaux objets Location.
    @Override
    public String toString(){
        return "ID: " + ID +", no_local: " + no_local +", adresse: " + adresse +", superficie: " + superficie +", annee_construction: " + annee_construction + ", status: " + status + ", dispo : " + disponible + ", date_debut : " + date_debut + ", date_fin : " + date_fin +", prix: " +prix_pied_carre ;
    }
}
