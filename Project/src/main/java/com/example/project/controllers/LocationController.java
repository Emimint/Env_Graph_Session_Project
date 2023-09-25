/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.MySqlConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationController implements EventHandler<ActionEvent>, Initializable {

    @FXML
    public Button addBtn;

    @FXML
    public Button saveBtn;

    @FXML
    public Button modifBtn;

    @FXML
    public Button delBtn;

    // On cree les colonnes du tableau dynamiquement:
    @FXML
    public TableView<Location> myTable;
    @FXML
    public TableColumn<Location, Integer> idColumn = new TableColumn<>("ID");
    @FXML
    public TableColumn<Location, String> localColumn = new TableColumn<>("N de local");
    @FXML
    public TableColumn<Location, String> adresseColumn = new TableColumn<>("Adresse");
    @FXML
    public TableColumn<Location, Integer> supColumn = new TableColumn<>("Superficie");
    @FXML
    public TableColumn<Location, Integer> anneeColumn = new TableColumn<>("Année de construction");

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Seul le bouton Ajouter sera visible au demarrage de la page:
        saveBtn.setVisible(false);
        modifBtn.setVisible(false);
        delBtn.setVisible(false);

        // Envoyer une requete SQL pour recuperer tous les champs de la table "locations":
        try {
            // Ajout du systeme de mapping aux futures colonnes du tableau:
            idColumn.setCellValueFactory(cellData -> cellData.getValue().getID().asObject());
            localColumn.setCellValueFactory(cellData -> cellData.getValue().no_localProperty());
            adresseColumn.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
            supColumn.setCellValueFactory(cellData -> cellData.getValue().getSuperficie().asObject());
            anneeColumn.setCellValueFactory(cellData -> cellData.getValue().getAnneeConstruction().asObject());

            // Ajout des colonnes du tableau:
            myTable.getColumns().addAll(idColumn, localColumn, adresseColumn, supColumn, anneeColumn);

            //Appel de la methode qui va populer les colonnes du tableau:
            populerTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populerTable() throws SQLException {
        PreparedStatement std = null;
        ResultSet resultat = null; // reste a null si rien n'a ete trouve
        try {
            // preparation du statement:
            std = MySqlConnection.getInstance().prepareStatement("select * from locations");

            // execution du statement:
            resultat = std.executeQuery();
            while (resultat.next()) {
                // Lecture rang par rang des donnees de la table Location.

                //1) recuperation des infos d'un rang pour creation d'un objet Location:
                Location row = new Location(Integer.parseInt(resultat.getString("id_location")), resultat.getString("num_local"), resultat.getString("adresse"), Integer.parseInt(resultat.getString("superficie")), Integer.parseInt(resultat.getString("annee_construction")));

                //2) ajout de l'objet cree a la table:
                myTable.getItems().add(row);
            }

        } catch (SQLException e) {
            Logger.getLogger(MySqlConnection.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            assert std != null;
            std.close();
            assert resultat != null;
            resultat.close();
        }
    }
}
