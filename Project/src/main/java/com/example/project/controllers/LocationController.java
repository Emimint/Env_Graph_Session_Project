/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocationController implements Initializable {

    @FXML
    public Button addBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button modifBtn;
    @FXML
    public Button delBtn;

    @FXML
    public Button clearBtn;

    @FXML
    public GridPane gridPane;

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
        // Seul le bouton Ajouter et le bouton Effacer selection seront visibles au demarrage de la page :
        saveBtn.setVisible(false);
        modifBtn.setVisible(false);
        delBtn.setVisible(false);

        // Le champ pour l'ID est desactive car la base de donnees gere les identifiants (auto-incrementation) :
        idField.getStyleClass().add("hidden");
        idField.setEditable(false);

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

        //Ajout d'une methode pour surveiller la selection des champs du tableau :
        myTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectionChamp();
        });
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
    public void ajouterLocation() {
        try {
            // On requere le prochain indice de la colonne ID via une requete SQL :
            int nextIndice = myLocationModel.getNextIndice();

            // On cree l'objet en utilisant l'indice et les valeurs entrees dans les differents champs :
            Location nouvelleLocation = new Location();
            nouvelleLocation.setID(nextIndice);
            nouvelleLocation.setNoLocal(localField.getText());
            nouvelleLocation.setAdresse(adresseField.getText());
            nouvelleLocation.setSuperficie(supField.getText());
            nouvelleLocation.setAnneeConstruction(anneeField.getText());
            // On appelle une boite de dialogue pour demander confirmation de l'ajout a l'utilisateur :
            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
            dialogC.setTitle("Ajout nouvelle location");
            dialogC.setHeaderText(null);
            dialogC.setContentText("Voulez-vous ajouter cette adresse?");
            Optional<ButtonType> answer = dialogC.showAndWait();
            if (answer.get() == ButtonType.OK) {
                myLocationModel.addLocation(nouvelleLocation);
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Nouvelle location ajoutee.");
                dialog.showAndWait();

                // On reload la nouvelle table:
                myTable.setItems(FXCollections.observableArrayList(myLocationModel.getListLocations()));

                // On vide les champs du Gridpane :
                viderChamps();

            }
            else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Annulation");
                dialog.setHeaderText("Annulation de l'ajout.");
                dialog.showAndWait();
            }
        } catch (IllegalArgumentException e){
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Attention : "+ e.getMessage());
            dialogW.showAndWait();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

      public void viderChamps() {
        // On commence par selectionner tous les champs de type Textfield dans le Gridpane :
        List<TextField> textFields = new ArrayList<>();
        for (Node node : gridPane.lookupAll(".text-field")) {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            }
        }

        // On boucle sur le resultat et on assigne une variable String vide :
        for (TextField textField : textFields) {
            textField.setText(""); // Set text to an empty string
        }
    }

    @FXML
    public void selectionChamp() {
        Location locationSelectionnee = myTable.getSelectionModel().getSelectedItem();
        if (locationSelectionnee != null) {
            int id = locationSelectionnee.getID();
            String noLocal = locationSelectionnee.getNo_local();
            String adresse = locationSelectionnee.getAdresse();
            int superficie = locationSelectionnee.getSuperficie();
            int anneeConstruction = locationSelectionnee.getAnnee_construction();

            // You can display the selected data in your UI or perform any other actions here
            // For example, set the values to the text fields:
            idField.setText(Integer.toString(id));
            localField.setText(noLocal);
            adresseField.setText(adresse);
            supField.setText(Integer.toString(superficie));
            anneeField.setText(Integer.toString(anneeConstruction));

            addBtn.setVisible(false);
            modifBtn.setVisible(true);
            delBtn.setVisible(true);
        }
    }

}
