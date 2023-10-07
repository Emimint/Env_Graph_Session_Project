package com.example.project.controllers;/*
 * @created 03/10/2023 - 5:26 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

import com.example.project.models.LocationModel;
import com.example.project.models.LoginModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class AddController implements Initializable {

    @FXML
    public Button saveBtn;
    @FXML
    public Button clrBtn;
    @FXML
    public Button quitBtn;

    @FXML
    public GridPane gridPane;

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

    @FXML
    public TextField debField;

    @FXML
    public TextField finField;

    @FXML
    public TextField prixField;

    public LocationModel myLocationModel = new LocationModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Le champ pour l'ID est desactive car la base de donnees gere les identifiants (auto-incrementation) :
        idField.getStyleClass().add("hidden");
        idField.setEditable(false);
    }

    //Appel de la methode pour ajouter une nouvelle adresse de location :
    @FXML
    public void ajouterLocation() {
        try {
            // On appelle une boite de dialogue pour demander confirmation de l'ajout a l'utilisateur :
            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
            dialogC.setTitle("Ajout nouvelle location");
            dialogC.setHeaderText(null);
            dialogC.setContentText("Voulez-vous ajouter cette adresse?");
            Optional<ButtonType> answer = dialogC.showAndWait();
            if (answer.get() == ButtonType.OK) {

                // On doit valider que tous les champs sont remplis (sauf le champs de l'ID) :
                for (Node node : gridPane.getChildren()) {
                    if (node instanceof TextField) {
                        if(((TextField) node).getText().trim().isEmpty() && !"idField".equals(node.getId()))
                        {
                            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                            dialog.setTitle("Annulation");
                            dialog.setHeaderText("Veuillez remplir tous les champs.\n");
                            dialog.showAndWait();
                            return;
                        }
                    }
                }

                // Avant de faire l'ajout, on verifie si cette adresse est deja dans la base de donnees:
                Location locationTrouve = myLocationModel.getLocationbyAdresse(adresseField.getText());

                if(locationTrouve != null ){
                // On autorise l'ajout que si certaines conditions sont bien respectees :
                boolean ajoutValide =
                        // 1) ... l'annee de construction est differente :
                        Objects.equals(anneeField.getText(), Integer.toString(locationTrouve.getAnnee_construction())) &&
                        // 2) ... l'annee de construction et le numero de local sont identiques :
                        !Objects.equals(localField.getText(), locationTrouve.getNo_local());
                if( !ajoutValide){
                    Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                    dialog.setTitle("Annulation");
                    dialog.setHeaderText("Votre requete ne peut etre acceptee. Veuillez verifier:\n" +
                            "1) tentez vous de rentrer un numero de local identique pour une adresse deja existante?\n" +
                            "2) l'annee de construction pour cette adresse devrait etre " +locationTrouve.getAnnee_construction() +".\n");
                    dialog.showAndWait();
                    return;
                }
                }
                // On cree l'objet en utilisant l'indice et les valeurs entrees dans les differents champs :
                Location nouvelleLocation = new Location();
                nouvelleLocation.setDisponible("True");
                nouvelleLocation.setDate_debut(debField.getText());
                nouvelleLocation.setDate_fin(finField.getText());
                nouvelleLocation.setStatus("True");
                nouvelleLocation.setNoLocal(localField.getText());
                nouvelleLocation.setAdresse(adresseField.getText());
                nouvelleLocation.setSuperficie(supField.getText());
                nouvelleLocation.setAnneeConstruction(anneeField.getText());
                nouvelleLocation.setPrix_pied_carre(prixField.getText());

                myLocationModel.addLocation(nouvelleLocation);
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Nouvelle location ajoutee.");
                dialog.showAndWait();
                viderChamps();
            }
            else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Annulation");
                dialog.setHeaderText("Annulation de l'ajout.");
                dialog.showAndWait();
            }
        } catch (IllegalArgumentException e){
            String content = "Attention : "+ e.getMessage();
            Alert dialogW = new Alert(Alert.AlertType.WARNING, content);
            dialogW.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.showAndWait();
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
    public void closeButtonAction(){
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    public void setLocationModel(LocationModel locationModel) {
        myLocationModel = locationModel;
    }

}
