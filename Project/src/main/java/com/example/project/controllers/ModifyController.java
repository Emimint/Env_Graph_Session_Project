/*
 * @created 03/10/2023 - 6:16 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import com.example.project.models.LoginModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyController implements Initializable {

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
    @FXML
    public CheckBox dispoBox;

    @FXML
    public Button saveBtn;

    @FXML
    public Button quitBtn;

    public Location myLocation;

    @Override
    public void initialize(URL url, ResourceBundle resources) {

        Platform.runLater(() -> {
        // Le champ pour l'ID est desactive pour empecher une modification par l'utilisateur courant :
        idField.setText(String.valueOf(myLocation.getID()));
        idField.getStyleClass().add("hidden");
        idField.setEditable(false);

        //On assigne les autres informations de la location aux differents champs :
        localField.setText(myLocation.getNo_local());
        adresseField.setText(myLocation.getAdresse());
        supField.setText(String.valueOf(myLocation.getSuperficie()));
        anneeField.setText(String.valueOf(myLocation.getAnnee_construction()));
        debField.setText(String.valueOf(myLocation.getDate_debut()));
        finField.setText(String.valueOf(myLocation.getDate_fin()));
        prixField.setText(String.valueOf(myLocation.getPrix_pied_carre()));
        });
    }

//    public void modifierLocation(){
//        try {
//            // On recupere l'indice de la colonne ID:
//            String indice = idField.getText();
//
//            // On cree l'objet en utilisant l'indice et les valeurs entrees dans les differents champs :
//            Location locationSelectionnee = new Location();
//            locationSelectionnee.setID(indice);
//            locationSelectionnee.setNoLocal(localField.getText());
//            locationSelectionnee.setAdresse(adresseField.getText());
//            locationSelectionnee.setSuperficie(supField.getText());
//            locationSelectionnee.setAnneeConstruction(anneeField.getText());
//
//            // On appelle une boite de dialogue pour demander confirmation la demande de modification a l'utilisateur :
//            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
//            dialogC.setTitle("Modification de la location #" + indice);
//            dialogC.setHeaderText(null);
//            dialogC.setContentText("Voulez-vous modifier cette adresse?");
//            Optional<ButtonType> answer = dialogC.showAndWait();
//            if (answer.get() == ButtonType.OK) {
//                myLocationModel.updateLocation(locationSelectionnee);
//                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
//                dialog.setTitle("Confirmation");
//                dialog.setHeaderText("Modification effectuee.");
//                dialog.showAndWait();
//
//            }
//            else {
//                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
//                dialog.setTitle("Annulation");
//                dialog.setHeaderText("Annulation de la modification.");
//                dialog.showAndWait();
//            }
//        } catch (IllegalArgumentException e){
//            Alert dialogW = new Alert(Alert.AlertType.WARNING);
//            dialogW.setTitle("Erreur");
//            dialogW.setHeaderText(null);
//            dialogW.setContentText("Attention : "+ e.getMessage());
//            dialogW.showAndWait();
//        }
//    }

    @FXML
    public void closeButtonAction(){
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    public void setLocation(Location location) {
        myLocation = location;
    }
}
