/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

package com.example.project.controllers;

import com.example.project.models.LocationModel;
import com.example.project.models.LoginModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class LocationController implements Initializable {

    private LoginModel loginModel;

    private Location locationSelectionnee;

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
    public Button refreshBtn;

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

    public LocationModel myLocationModel = new LocationModel();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //On commence par personnaliser l'affichage avec le prenom et l'image de l'utilisateur :
        Platform.runLater(() -> {

            // Si l'utilisateur connecte n'est pas un administrateur, on lui retire les droits de deletion d'une location dans la base de donnees :
            if(!Objects.equals(loginModel.getPrenom(), "Admin")) {
                delBtn.setVisible(false);
                deleteMenu.setDisable(true);
            } else {
                delBtn.setVisible(true);
                deleteMenu.setDisable(false);
            }

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

            //Ajout d'une methode pour surveiller la selection des champs du tableau :
            myTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                locationSelectionnee = myTable.getSelectionModel().getSelectedItem();
            });
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
            customiserTableau(dispoColumn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        // On interrompt l'execution du programme si aucune location n'est selectionnee :
        if (locationSelectionnee == null) {
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Aucune location n'est sélectionnée.");
            dialogW.showAndWait();
            return;
        }

        // On s'assure que la nouvelle fenetre sera la seule active :
        Stage myStage = new Stage();
        myStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Modify.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //On transmet les informations du LocationModel au nouveau controleur:
        ModifyController modifyController = fxmlLoader.getController();
        modifyController.setLocation(locationSelectionnee);

        myStage.setTitle("Modifier une nouvelle location existante");
        myStage.setScene(scene);
        myStage.show();
    }


    public void supprimerLocation(){
        try {
            // On recupere la location courante:
            locationSelectionnee = myTable.getSelectionModel().getSelectedItem();

            String indice = String.valueOf(locationSelectionnee.getID());

            // On appelle une boite de dialogue pour demander confirmation la demande de suppression a l'utilisateur :
            Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
            dialogC.setTitle("Suppression de la location #" + indice);
            dialogC.setHeaderText(null);
            dialogC.setContentText("Voulez-vous supprimer definitivement cette adresse?");
            Optional<ButtonType> answer = dialogC.showAndWait();
            if (answer.get() == ButtonType.OK) {
                myLocationModel.deleteLocation(Integer.parseInt(indice));
                locationSelectionnee = null;
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Confirmation");
                dialog.setHeaderText("Adresse supprimee de la base de donnees.");
                dialog.showAndWait();
            }
            else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Annulation");
                dialog.setHeaderText("Annulation de la suppression.");
                dialog.showAndWait();
            }
        } catch (IllegalArgumentException | SQLException e){
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Attention : "+ e.getMessage());
            dialogW.showAndWait();
        } catch (NullPointerException ex){
            Alert dialogW = new Alert(Alert.AlertType.WARNING);
            dialogW.setTitle("Erreur");
            dialogW.setHeaderText(null);
            dialogW.setContentText("Aucune location n'est selectionnee.");
            dialogW.showAndWait();
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

    public void creerRapportStandard() throws SQLException {
        genererRapport(myLocationModel.getListLocations());
    }

    // Methode pour griser les locations indisponibles :
    private void customiserTableau(TableColumn<Location, Boolean> tableColumn) {
        tableColumn.setCellFactory(column -> {
            return new TableCell<Location, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);

                    TableRow<Location> currentRow = getTableRow();

                    if (!isEmpty()) {

                        if(!item)
                            currentRow.setStyle("-fx-background-color:rgba(0,0,0,0.2)");
                    }
                }
            };
        });
    }


    public void genererRapport(List<Location> liste_de_locations){
        try
        {
            // 1. Creer le nom du fichier :
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HHmmss");
            String timestamp = now.format(formatter);
            String output_dir = "/src/data/output"; // repertoire de destination
            String filename = output_dir + "/rapport_du_" +timestamp+".pdf";

            // 2. Creer le chemin d'acces au fichier :
            String dir = System.getProperty("user.dir"); // repertoire du project
            File file = new File(dir,filename);

            // 3. Creation du fichier :
            Document my_pdf_report = new Document();
            my_pdf_report.setPageSize(PageSize.A4.rotate());
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream(file));
            my_pdf_report.open();
            PdfPTable my_report_table = new PdfPTable(new float[] { 0.5f, 2.5f,0.5f,1,1,1,1,1,1,1}); // 10 colonnes de la table "locations" (avec dimensions customisees par contenu)
            PdfPCell table_cell;

            // On commence par mettre un titre :
            formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            timestamp = now.format(formatter);

            Paragraph preface = new Paragraph("Rapport standard de locations du " + timestamp +":\n\n");
            preface.setAlignment(Element.ALIGN_CENTER);
            my_pdf_report.add(preface);

            // 4. Creation du header :
            table_cell=new PdfPCell(new Phrase("ID"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Adresse"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("#loc"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Sup.(p2)"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Constr."));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Status"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Dispo."));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Loue du"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("au"));
            my_report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("$/p2"));
            my_report_table.addCell(table_cell);

            // 5. Instanciation du tableau :
            for (Location location: liste_de_locations
                 ) {
                String id_location = String.valueOf(location.getID());
                table_cell=new PdfPCell(new Phrase(id_location));
                my_report_table.addCell(table_cell);
                String adresse=location.getAdresse();
                table_cell=new PdfPCell(new Phrase(adresse));
                my_report_table.addCell(table_cell);
                String num_local=location.getNo_local();
                table_cell=new PdfPCell(new Phrase(num_local));
                my_report_table.addCell(table_cell);
                String superficie= String.valueOf(location.getSuperficie());
                table_cell=new PdfPCell(new Phrase(superficie));
                my_report_table.addCell(table_cell);
                String annee_construction= String.valueOf(location.getAnnee_construction());
                table_cell=new PdfPCell(new Phrase(annee_construction));
                my_report_table.addCell(table_cell);
                String status_location=location.getStatus()? "loue": "non loue";
                table_cell=new PdfPCell(new Phrase(status_location));
                my_report_table.addCell(table_cell);
                String disponibilite=location.getDisponible()? "disponible": "indisponible";
                table_cell=new PdfPCell(new Phrase(disponibilite));
                my_report_table.addCell(table_cell);
                String date_debut= location.getDate_debut()> 0? String.valueOf(location.getDate_debut()): "N/A";
                table_cell=new PdfPCell(new Phrase(date_debut));
                my_report_table.addCell(table_cell);
                String date_fin= location.getDate_fin()> 0? String.valueOf(location.getDate_fin()): "N/A";
                table_cell=new PdfPCell(new Phrase(date_fin));
                my_report_table.addCell(table_cell);
                String prix_pied_carre= String.valueOf(location.getPrix_pied_carre());
                table_cell=new PdfPCell(new Phrase(prix_pied_carre));
                my_report_table.addCell(table_cell);
            }

            // 6. Ajout du tableau cree au fichier :
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();

            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            dialog.setTitle("Confirmation");
            dialog.setHeaderText("Rapport cree avec succes.");
            dialog.showAndWait();

        } catch (Exception e)
        {
            System.out.println("Erreur a la creation du rapport standard: " +e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void rafraichirTableau() {
        try {
            myTable.setItems(FXCollections.observableArrayList(myLocationModel.getListLocations()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void quitter(){
        System.exit(0);
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

}
