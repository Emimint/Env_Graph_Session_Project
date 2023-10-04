/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import com.example.project.models.LoginModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class LocationController implements Initializable {

    private LoginModel loginModel;

    @FXML
    public BorderPane myPane;

    @FXML
    public Button modifBtn;
    @FXML
    public Button delBtn;

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
    public TableColumn<Location, Boolean> statusColumn;
    @FXML
    public TableColumn<Location, Boolean> dispoColumn;
    @FXML
    public TableColumn<Location, Integer> debColumn;
    @FXML
    public TableColumn<Location, Integer> finColumn;
    @FXML
    public TableColumn<Location, Integer> prixColumn;

    @FXML
    public MenuButton userBtn; // bouton de personnalisation (inclus dans un bouton-menu avec bouton de deconnexion)

    @FXML
    public ImageView userImg;

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

        //On commence par personnaliser l'affichage avec le prenom et l'image de l'utilisateur :
        Platform.runLater(() -> {

            // 1) On affiche le prenom dans le menu :
            userBtn.setText(loginModel.getPrenom());

            // 2) On recupere l'image de profile de l'utlisateur courant:

            // On recupere d'abord le nom du fichier :
            String userIDfilename = "/img/user_0" + loginModel.getUserID() + ".jpg";
            InputStream stream = getClass().getResourceAsStream(userIDfilename);

            if (stream != null) {
                Image image = new Image(stream);
                userImg.setImage(image);
            } else {
                System.err.println("Image non trouvee : " + userIDfilename);
            }
            //On rajoute des parametres pour correctement afficher l'image:
            Rectangle clip = new Rectangle(
                    userImg.getFitWidth(), userImg.getFitHeight()
            );
            clip.setArcWidth(10);
            clip.setArcHeight(10);
            userImg.setClip(clip);
        });


        // Ajout d'une methode pour surveiller les clicks de souris en dehors du tableau :
        myPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (!myTable.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
                myTable.getSelectionModel().clearSelection();
            }
        });

        // Ajout du systeme de mapping aux futures colonnes du tableau (les proprietes doivent correspondre aux noms exacts des attributs de Location) :
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        localColumn.setCellValueFactory(new PropertyValueFactory<>("no_local"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        supColumn.setCellValueFactory(new PropertyValueFactory<>("Superficie"));
        anneeColumn.setCellValueFactory(new PropertyValueFactory<>("annee_construction"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dispoColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        debColumn.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        finColumn.setCellValueFactory(new PropertyValueFactory<>("date_fin"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix_pied_carre"));

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

    //Appel de la methode pour le retour à l'écran de connexion :
    @FXML
    public void retourLogin() throws IOException {
        modifBtn.getScene().getWindow().hide(); // recuperation de la scene en cours via n'importe quel element (ici, le bouton Ajouter)
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
            String nextIndice = String.valueOf(myLocationModel.getNextIndice());

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
                // Avant de faire l'ajout, on verifie si cette adresse est deja dans la base de donnees:
                Location locationTrouve = myLocationModel.getLocationbyAdresse(nouvelleLocation);

                // On autorise l'ajout que si certaines conditions sont bien respectees :
                if( locationTrouve != null && !ajoutValide(nouvelleLocation, locationTrouve)){
                    Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                    dialog.setTitle("Annulation");
                    dialog.setHeaderText("Votre requete ne peut etre acceptee. Veuillez verifier:\n" +
                            "1) tentez vous de rentrer un numero de local identique pour une adresse deja existante?\n" +
                            "2) l'annee de construction pour cette adresse devrait etre " +nouvelleLocation.getAnnee_construction() +".\n");
                    dialog.showAndWait();
                    return;
                }
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

    public void modifierLocation(){
        try {
            // On recupere l'indice de la colonne ID:
            String indice = idField.getText();

            // On cree l'objet en utilisant l'indice et les valeurs entrees dans les differents champs :
            Location locationSelectionnee = new Location();
            locationSelectionnee.setID(indice);
            locationSelectionnee.setNoLocal(localField.getText());
            locationSelectionnee.setAdresse(adresseField.getText());
            locationSelectionnee.setSuperficie(supField.getText());
            locationSelectionnee.setAnneeConstruction(anneeField.getText());

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

                // On reload la nouvelle table:
                myTable.setItems(FXCollections.observableArrayList(myLocationModel.getListLocations()));
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ajoutValide(Location a, Location b){
        // On retourne faux si l'adresse est identique et que...
        // 1) ... l'annee de construction est differente :
        return a.getAnnee_construction() == b.getAnnee_construction() &&
// 2) ... l'annee de construction et le numero de local sont identiques :
       !Objects.equals(a.getNo_local(), b.getNo_local());
    }

    public void supprimerLocation(){
        try {
            // On recupere l'indice de la colonne ID:
            String indice = idField.getText();

            Location locationSelectionnee = myTable.getSelectionModel().getSelectedItem();

            // On appelle une boite de dialogue pour demander confirmation la demande de suppression a l'utilisateur :
            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
            dialogC.setTitle("Suppression de la location #" + indice);
            dialogC.setHeaderText(null);
            dialogC.setContentText("Voulez-vous supprimer cette adresse?");
            Optional<ButtonType> answer = dialogC.showAndWait();
            if (answer.get() == ButtonType.OK) {
                myLocationModel.deleteLocation(Integer.parseInt(indice));
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Adresse supprimee.");
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
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Attention : "+ e.getMessage());
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
    public void selectionChamp() {
        Location locationSelectionnee = myTable.getSelectionModel().getSelectedItem();
        if (locationSelectionnee != null) {
            int id = locationSelectionnee.getID();
            String noLocal = locationSelectionnee.getNo_local();
            String adresse = locationSelectionnee.getAdresse();
            int superficie = locationSelectionnee.getSuperficie();
            int anneeConstruction = locationSelectionnee.getAnnee_construction();

            // On affiche les informations recuperees :
            idField.setText(Integer.toString(id));
            localField.setText(noLocal);
            adresseField.setText(adresse);
            supField.setText(Integer.toString(superficie));
            anneeField.setText(Integer.toString(anneeConstruction));
        }
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

}
