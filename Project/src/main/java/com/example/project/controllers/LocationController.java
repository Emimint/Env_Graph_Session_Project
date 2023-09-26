/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import com.example.project.models.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationController implements Initializable {

    @FXML
    public Button addBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button modifBtn;
    @FXML
    public Button delBtn;

    // On cree les colonnes du tableau dynamiquement :
    @FXML
    public TableView<Location> myTable;
    @FXML
    public TableColumn<Location, Integer> idColumn;
    @FXML
    public TableColumn<Location, String> localColumn;
    @FXML
    public TableColumn<Location, String> adresseColumn;
    @FXML
    public TableColumn<Location, Integer> supColumn;
    @FXML
    public TableColumn<Location, Integer> anneeColumn;

    @FXML
    public MenuItem disconnectBtn; // bouton de deconnexion (inclus dans un bouton-menu)

    // On ajoute les champs pour la creation ou la sauvegarde d'un nouvel objet Location :
    @FXML
    public TextField idField;
    @FXML
    public TextField localField;
    @FXML
    public TextField adresseField;
    @FXML
    public TextField supField;
    @FXML
    public TextField anneeField;

    public LocationModel myLocationModel = new LocationModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Seul le bouton Ajouter sera visible au demarrage de la page :
        saveBtn.setVisible(false);
        modifBtn.setVisible(false);
        delBtn.setVisible(false);

        // Ajout du systeme de mapping aux futures colonnes du tableau (les proprietes doivent correspondre aux noms exacts des attributs de Location) :
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("no_local"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        supColumn.setCellValueFactory(new PropertyValueFactory<>("Superficie"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("annee_construction"));

        // Envoyer une requete SQL pour recuperer tous les champs de la table "locations" :
        try {
            // Ajout des colonnes du tableau :
            myTable.setItems(FXCollections.observableArrayList(myLocationModel.getListLocations()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viderTable() {
        // vidange de la totalite des donnees du table (si il y en a) :
        myTable.getItems().clear();
    }

    //Appel de la methode pour le retour à l'écran de connexion :
    @FXML
    public void retourLogin() throws IOException {
        addBtn.getScene().getWindow().hide(); // recuperation de la scene en cours via n'importe quel element (ici, le bouton Ajouter)
        Stage myStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 440);
        myStage.setTitle("Connection");
        myStage.setScene(scene);
        myStage.show();
    }

    //Appel de la methode pour ajouter une nouvelle adresse de location :
    @FXML
    public void ajouterLocation() throws IOException {
        try {
            Location nouvelleLocation = new Location(Integer.parseInt(idField.getText()), localField.getText(), adresseField.getText(),Integer.parseInt(supField.getText()), Integer.parseInt(anneeField.getText()));
            myLocationModel.addLocation(nouvelleLocation);
        } catch (Exception e){
            return; // popup avec alert ici!
        }
    }
}
