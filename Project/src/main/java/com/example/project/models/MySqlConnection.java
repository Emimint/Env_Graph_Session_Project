package com.example.project.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySqlConnection {
    // Creation de la variable de connexion à la base de données MySql:
    private static String DRIVER ="com.mysql.cj.jdbc.Driver",
            URL = "jdbc:mysql://localhost:3306/gestion_location?serverTimezone=UTC",
    // il faut modifier le champ user et password en fonction des acces propres a chaque utilisateur:
            USER = "root",
            PASSWORD = "root";

    private static Connection cnx = null;

    public static Connection getInstance() {
        try {
            if (cnx == null || cnx.isClosed()) {
                Class.forName(DRIVER);
                cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySqlConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySqlConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cnx;
    }

}
