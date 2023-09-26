package com.example.project.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginModel {
    // Responsable de se connecter à la base de donnees, pour l'affichage de la BD :
    Connection connection;
    public LoginModel(){
        connection = MySqlConnection.getInstance();
    }

    // verifier si la connection est bien etablie :
    public boolean isDBConnected(){
        try{
            return !connection.isClosed();
        }catch (SQLException e){
            return false;
        }
    }

    public boolean LoginNow(String user, String password) throws SQLException {
        PreparedStatement std =null;
        ResultSet resultat = null; // reste a null si rien n'a ete trouve
        try{
            // preparation du statement :
            std = connection.prepareStatement("select * from utilisateurs where nom =? and mot_de_passe =?");
            std.setString(1,user);
            std.setString(2,password);

            // execution du statement :
            resultat = std.executeQuery();
            return resultat.next();

        }catch (SQLException e) {
            Logger.getLogger(MySqlConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        finally {
            assert std !=null;
            std.close();
            assert resultat !=null;
            resultat.close();
        }
        return false;
    }
}


