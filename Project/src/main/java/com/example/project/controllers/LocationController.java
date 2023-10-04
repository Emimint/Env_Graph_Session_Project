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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class LocationController implements Initializable {

    private LoginModel loginModel;

    private String prenomUser;

    // Buttons de la bar de navigation :
    @FXML
    public MenuItem ajoutMenu;
    @FXML
    public MenuItem modifMenu;
    @FXML
    public MenuItem deleteMenu;
    @FXML
    public MenuItem rapportStd;
    @FXML
    public MenuItem searchMenu;
    @FXML
    public MenuItem aboutMenu;
    @FXML
    public MenuItem quitMenu;


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

    public void ouvrirAjouter() throws IOException {
        // On s'assure que la nouvelle fenetre sera la seule active :
        Stage myStage = new Stage();
        myStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Add.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //On transmet les informations du LocationModel au nouveau controleur:
        AddController ajouterController = fxmlLoader.getController();
        ajouterController.setLocationModel(myLocationModel);

        myStage.setTitle("Ajouter une nouvelle location");
        myStage.setScene(scene);
        myStage.show();
    }

    //Appel de la methode pour le retour à l'écran de connexion :
    @FXML
    public void retourLogin() throws IOException {
        modifBtn.getScene().getWindow().hide(); // recuperation de la scene en cours via n'importe quel element (ici, le bouton de sauvegarde)
        Stage myStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 620, 440);
        myStage.setTitle("Connection");
        myStage.setScene(scene);
        myStage.show();
    }

    public void ouvrirModifier() throws IOException {
        // On s'assure que la nouvelle fenetre sera la seule active :
        Stage myStage = new Stage();
        myStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Modify.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //On transmet les informations du LocationModel au nouveau controleur:
        ModifyController modifyController = fxmlLoader.getController();
        modifyController.setLocationModel(myLocationModel);

        myStage.setTitle("Modifier une nouvelle location existante");
        myStage.setScene(scene);
        myStage.show();
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
            }
            else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Annulation");
                dialog.setHeaderText("Annulation de l'ajout.");
                dialog.showAndWait();
            }
        } catch (IllegalArgumentException | SQLException e){
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Attention : "+ e.getMessage());
            dialogW.showAndWait();
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

    public void ouvrirReadMe() throws IOException {
        try{
        // On s'assure que la nouvelle fenetre sera la seule active :
        Stage myStage = new Stage();
        myStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/ReadMe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        myStage.setTitle("A propos de ce projet");
        myStage.setScene(scene);
        myStage.show();
        } catch (IOException  e) {
            System.out.println("Erreur: " +e);
            e.printStackTrace();
        }
    }

    public void quitter(){
        System.exit(0);
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

}
