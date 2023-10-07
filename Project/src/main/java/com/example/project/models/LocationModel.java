package com.example.project.models;

import com.example.project.controllers.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationModel {
    Connection connection;
    public LocationModel(){
        connection = MySqlConnection.getInstance();
    }

public List<Location>  getListLocations() throws SQLException {
    List<Location> locations = new ArrayList<>();
    PreparedStatement std = null;
    ResultSet resultat = null;

    try{
        std = connection.prepareStatement("select * from locations");
        resultat = std.executeQuery();

        while (resultat.next()){
            Location location = new Location(resultat.getInt("id_location"), resultat.getString("num_local"), resultat.getString("adresse"), resultat.getInt("superficie"), resultat.getInt("annee_construction"), resultat.getBoolean("status_location") ? "loue": "non loue", resultat.getBoolean("disponibilite")? "": "indisponible", resultat.getInt("date_debut"), resultat.getInt("date_fin"), resultat.getInt("prix_pied_carre"));
            locations.add(location);
        }
        std.close();
        resultat.close();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return locations;
}

    public void addLocation(Location location) {
        PreparedStatement std = null;

        try {
            // La base de donnees etant auto-incrementeE, on n'a pas besoin de manuellement ajouter l'ID :
            std = connection.prepareStatement("INSERT INTO locations(num_local, adresse, superficie, annee_construction, status_location, disponibilite, date_debut, date_fin, prix_pied_carre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
            std.setString(1, location.getNo_local());
            std.setString(2, location.getAdresse());
            std.setInt(3, location.getSuperficie());
            std.setInt(4, location.getAnnee_construction());
            std.setBoolean(5, location.getStatus() == "loue");
            std.setBoolean(6, location.getDisponible()== "");
            std.setInt(7, location.getDate_debut());
            std.setInt(8, location.getDate_fin());
            std.setInt(9, location.getPrix_pied_carre());

            int nbrRangsModifies = std.executeUpdate();
            if (nbrRangsModifies == 0) {
                throw new SQLException("Echec ajout.");
            }

            std.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLocation(Location location){
        PreparedStatement std = null;

        try {
            std = connection.prepareStatement("UPDATE locations \n" +
                    "SET  num_local =? , adresse=?, superficie=?, annee_construction=? " +
                    "WHERE id_location = ?;");
            std.setString(1, location.getNo_local());
            std.setString(2, location.getAdresse());
            std.setInt(3, location.getSuperficie());
            std.setInt(4, location.getAnnee_construction());
            std.setInt(5, location.getID());

            int nbrRangsModifies = std.executeUpdate();
            if (nbrRangsModifies == 0) {
                throw new SQLException("Echec de la modification.");
            }

            std.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Location getLocationbyID(int indice){
        PreparedStatement std = null;
        ResultSet resultat = null;
        Location location = null;

        try {
            std = connection.prepareStatement("SELECT * FROM locations WHERE id_location = ?;");
            std.setInt(1, indice);

            resultat = std.executeQuery();
            while (resultat.next()){
                location = new Location(resultat.getInt("id_location"), resultat.getString("num_local"), resultat.getString("adresse"), resultat.getInt("superficie"), resultat.getInt("annee_construction"), resultat.getBoolean("status_location")? "loue": "non loue", resultat.getBoolean("disponibilite")? "": "indisponible", resultat.getInt("date_debut"), resultat.getInt("date_fin"), resultat.getInt("prix_pied_carre"));
            }
            std.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return location;
    }

    public Location getLocationbyAdresse(String adresse){
        PreparedStatement std = null;
        ResultSet resultat = null;
        Location location = null;

        try {
            // 1. Si l'adresse existe deja et que le numero de batiment est le meme :
            std = connection.prepareStatement("SELECT * FROM locations WHERE adresse LIKE ?;");
            std.setString(1, "%" + adresse + "%");

            resultat = std.executeQuery();
            if (resultat.next()) {
                location = new Location(resultat.getInt("id_location"), resultat.getString("num_local"), resultat.getString("adresse"), resultat.getInt("superficie"), resultat.getInt("annee_construction"), resultat.getBoolean("status_location")? "loue": "non loue", resultat.getBoolean("disponibilite")? "": "indisponible", resultat.getInt("date_debut"), resultat.getInt("date_fin"), resultat.getInt("prix_pied_carre"));
            }
            std.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return location;
    }

    public void deleteLocation(int indice) throws SQLException {
        PreparedStatement std = null;

        try {
            std = connection.prepareStatement("DELETE FROM locations WHERE id_location=?;");
            std.setInt(1, indice);
            std.executeUpdate();
            std.close();
        } catch (SQLException e) {
            throw new SQLException("Echec de la suppression.");
        }
    }


    public int getNextIndice() {
        PreparedStatement std = null;
        ResultSet resultat = null;
        int indice=-1;

        try {
            std = connection.prepareStatement("SELECT LAST_INSERT_ID();");

            resultat = std.executeQuery();
            if (resultat.next()) {
                indice = resultat.getInt(1);
            }

            std.close();
            resultat.close();
        } catch (SQLException e) {
            System.out.println("Erreur: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return indice;
    }

}
