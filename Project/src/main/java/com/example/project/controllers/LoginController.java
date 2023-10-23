package com.example.project.controllers;

import com.example.project.models.LoginModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public LoginModel loginModel = new LoginModel();

    @FXML
    private TextField usrName;

    @FXML
    private Label myLoginLabel;

    @FXML
    private PasswordField pwdField;

    @FXML
    private Button connectBtn;

    @FXML
    private Label infoLabel;

    public LoginController() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (loginModel.isDBConnected()) {
                myLoginLabel.setText("Connection a la BD reussie.");
                myLoginLabel.getStyleClass().add("text-glow");
                myLoginLabel.getStyleClass().add("success");
            } else {
                //En cas de mauvaise connexion :

                // 1. On affiche un message d'erreur :
                myLoginLabel.setText("Connection BD non établie!");
                myLoginLabel.getStyleClass().add("text-glow");
                myLoginLabel.getStyleClass().add("error");

                // 2. On desactive tous les boutons :
                connectBtn.setDisable(true);
                connectBtn.setStyle("-fx-opacity: 0.5;");

                usrName.getStyleClass().add("hidden");
                usrName.setEditable(false);

                pwdField.getStyleClass().add("hidden");
                pwdField.setEditable(false);
            }
        } catch (Exception e) {
            System.out.println("Erreur: " +e.getMessage());
        }
    }

    @FXML
    protected void LoginCheck(ActionEvent event) throws IOException {
        try {
            if (loginModel.LoginNow(usrName.getText(), pwdField.getText())) {
                ((Node) (event.getSource())).getScene().getWindow().hide();

                FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Main.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage myStage = new Stage();

                //On transmet les informations du LoginModel au nouveau controleur :
                LocationController locationController = fxmlLoader.getController();
                locationController.setLoginModel(loginModel);

                scene.getStylesheets().add("style.css");
                myStage.setTitle("Gestion des locations");
                myStage.setResizable(false);
                myStage.setScene(scene);
                myStage.show();
            } else infoLabel.setText("Nom d'utilisateur ou mot de passe invalide(s).");
        } catch (SQLException e) {
            System.out.println("Erreur: " +e.getMessage());
            infoLabel.setText("Erreur de connexion à la base de données.");
            infoLabel.getStyleClass().add("error");
        }
    }

    public void onEnter(ActionEvent event) throws IOException {
        LoginCheck(event);
    }
}