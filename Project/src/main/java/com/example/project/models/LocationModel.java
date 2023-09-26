package com.example.project.models;

import com.example.project.controllers.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

            Location location = new Location(resultat.getInt("id_location"), resultat.getString("num_local"), resultat.getString("adresse"), resultat.getInt("superficie"), resultat.getInt("annee_construction"));
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
            std = connection.prepareStatement("INSERT INTO locations(id_location, num_local, adresse, superficie, annee_construction) VALUES (?, ?, ?, ?, ?);");
            std.setInt(1, location.getID());
            std.setString(2, location.getNo_local());
            std.setString(3, location.getAdresse());
            std.setInt(4, location.getSuperficie());
            std.setInt(5, location.getAnnee_construction());

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
