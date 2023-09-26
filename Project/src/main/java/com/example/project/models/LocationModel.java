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
    public void addLocation(Location location){
        PreparedStatement std = null;
        ResultSet resultat = null;

        try{
            std = connection.prepareStatement("INSERT INTO locations(id_location, num_local, adresse,superficie, annee_construction) VALUES (default, ?, ?, ?, ?);");
            std.setString(2,location.getNo_local());
            std.setString(3,location.getAdresse());
            std.setInt(4,location.getSuperficie());
            std.setInt(5,location.getAnneeConstruction());

            resultat = std.executeQuery();
            std.close();
            resultat.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
