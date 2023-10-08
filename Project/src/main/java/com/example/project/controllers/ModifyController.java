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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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

    public LocationModel myLocationModel;

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
        if(myLocation.getDisponible()) dispoBox.setSelected(true);
        });
    }

    public void modifierLocation(){
        try {
            // On recupere l'indice de la colonne ID:
            String indice = idField.getText();

            // On cree l'objet en utilisant l'indice et les valeurs entrees dans les differents champs :
            Location locationSelectionnee = new Location();
            locationSelectionnee.setID(indice);
            locationSelectionnee.setDisponible(String.valueOf(dispoBox.isSelected()));
            locationSelectionnee.setNoLocal(localField.getText());
            locationSelectionnee.setAdresse(adresseField.getText());
            locationSelectionnee.setSuperficie(supField.getText());
            locationSelectionnee.setAnneeConstruction(anneeField.getText());
            locationSelectionnee.setDate_debut(debField.getText());
            locationSelectionnee.setDate_fin(finField.getText());
            locationSelectionnee.setPrix_pied_carre(prixField.getText());


            // On appelle une boite de dialogue pour demander confirmation la demande de modification a l'utilisateur :
            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
            dialogC.setTitle("Modification de la location #" + indice);
            dialogC.setHeaderText(null);
            dialogC.setContentText("Voulez-vous modifier cette adresse?");
            Optional<ButtonType> answer = dialogC.showAndWait();
            if (answer.get() == ButtonType.OK) {
                myLocationModel.updateLocation(locationSelectionnee);
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Modification effectuee.");
                dialog.showAndWait();

                // On ferme la fenetre (a utilise n'importe quel noeud pour trouver la scene) :
                idField.getScene().getWindow().hide();
            }
            else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Annulation");
                dialog.setHeaderText("Annulation de la modification.");
                dialog.showAndWait();
            }
        } catch (IllegalArgumentException e){
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Attention : "+ e.getMessage());
            dialogW.showAndWait();
        }
    }

    public void changerDisponibilite() {
        if (!dispoBox.isSelected()) {

            // On cree un boite de dialogue personnalisee :
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Rapport d'indisponibilité");
            dialog.setHeaderText("Attention, vous allez retirer cette adresse de la banque de locations. Veuillez indiquer le motif approprié:");
            VBox vbox = new VBox();
            vbox.setSpacing(10.0);

            // On cree ensuite un groupe d'options... :
            ToggleGroup motifGroup = new ToggleGroup();

            // ... et la liste des motifs :
            RadioButton motif1 = new RadioButton("Passage au feu");
            motif1.setToggleGroup(motifGroup);

            RadioButton motif2 = new RadioButton("Défaut d'eau");
            motif2.setToggleGroup(motifGroup);

            RadioButton motif3 = new RadioButton("Insalubre (Enquête ville)");
            motif3.setToggleGroup(motifGroup);

            RadioButton motif4 = new RadioButton("Changement de contractant");
            motif4.setToggleGroup(motifGroup);

            RadioButton motif5 = new RadioButton("Autre");
            motif5.setToggleGroup(motifGroup);

            vbox.getChildren().addAll(motif1, motif2, motif3, motif4, motif5);

            dialog.getDialogPane().setContent(vbox);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

            dialog.getDialogPane().setMinWidth(vbox.getBoundsInParent().getWidth() + 20);
            dialog.getDialogPane().setMinHeight(vbox.getBoundsInParent().getHeight() + 20);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == okButtonType) {
                    RadioButton selectedRadioButton = (RadioButton) motifGroup.getSelectedToggle();
                    myLocation.setDisponible("False");

                    // On met a jour la location dans la base de donnees :
                    myLocationModel.updateLocation(myLocation.getID(), 0, myLocation.getDate_debut(), myLocation.getDate_fin());

                    Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
                    confirmationDialog.setTitle("Confirmation");
                    confirmationDialog.setHeaderText("Changement de disponibilité effectué.");
                    confirmationDialog.showAndWait();
                    // On ferme la fenetre (a utilise n'importe quel noeud pour trouver la scene) :
                    dispoBox.getScene().getWindow().hide();
                }
                return null;
            });
            // On ferme la fenetre (a utilise n'importe quel noeud pour trouver la scene) :
            dispoBox.getScene().getWindow().hide();
            dialog.showAndWait();
        }
    }


    @FXML
    public void closeButtonAction(){
        Stage stage = (Stage) quitBtn.getScene().getWindow();
        stage.close();
    }

    public void setLocation(Location location) {
        myLocation = location;
    }

    public void setLocationModel(LocationModel locationModel) {
        myLocationModel = locationModel;
    }
}
