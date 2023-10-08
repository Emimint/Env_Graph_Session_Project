/*
 * @created 08/10/2023 - 10:24 a.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;

import java.net.URL;
import java.util.*;

public class SearchController {

    public LocationModel myLocationModel = new LocationModel();
    
    @FXML
    public GridPane gridPane;
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
    public TextField debutPrix;
    @FXML
    public TextField finPrix;


    @FXML
    public CheckBox statusBox;
    @FXML
    public CheckBox dispoBox;

    @FXML
    public Button saveBtn;
    @FXML
    public Button quitBtn;

    @FXML
    public void closeButtonAction(){
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    public void chercherLocation(){
        String requete ="";

        requete += !localField.getText().trim().isEmpty()?
                "num_local=\"" +localField.getText().trim() + "\" and "
                :"";
        requete += !adresseField.getText().trim().isEmpty()?
                "adresse=\"" +adresseField.getText().trim() + "\" and "
                :"";
        requete += !supField.getText().trim().isEmpty()?
                "superficie=" +supField.getText().trim() + " and "
                :"";
        requete += !anneeField.getText().trim().isEmpty()?
                "annee_construction=" +anneeField.getText().trim() + " and "
                :"";
        requete += !debField.getText().trim().isEmpty()?
                "date_debut=" +debField.getText().trim() + " and "
                :"";
        requete += !finField.getText().trim().isEmpty()?
                "date_fin=" +finField.getText().trim() + " and "
                :"";

        requete += statusBox.isSelected()? "status_location=1 and " :
                "status_location=0 and ";
        requete += dispoBox.isSelected()? "disponibilite=1 and " : "disponibilite=0 and ";

        String minPrix = debutPrix.getText().trim();
        String maxPrix = finPrix.getText().trim();

        if(!minPrix.isEmpty() && !maxPrix.isEmpty()){
            requete += "prix_pied_carre <=" +maxPrix + " and prix_pied_carre >="
                    + minPrix;
        }
        else if (minPrix.isEmpty() && !maxPrix.isEmpty()){
            requete += "prix_pied_carre <=" +maxPrix;
        }
        else if(!minPrix.isEmpty()) {
            requete += "prix_pied_carre >=" + minPrix;
        }
        System.out.println(requete);
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


    public void setLocationModel(LocationModel locationModel) {
        myLocationModel = locationModel;
    }
}
