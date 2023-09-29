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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle rb){
        if(loginModel.isDBConnected()){
            myLoginLabel.setText("Connection a la BD reussie.");
        } else {
            myLoginLabel.setText("Connection BD non etablie!");
            myLoginLabel.setTextFill(Color.color(1, 0, 0));
        }
    }

    public LoginModel loginModel = new LoginModel();

    @FXML
    protected void LoginCheck(ActionEvent event) throws SQLException, IOException {
        if(loginModel.LoginNow(usrName.getText(), pwdField.getText())){
            ((Node)(event.getSource())).getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(com.example.project.MainApplication.class.getResource("views/Main.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage myStage = new Stage();

            //On transmet les informations du LoginModel au nouveau controleur:
            LocationController locationController = fxmlLoader.getController();
            locationController.setLoginModel(loginModel);

//            scene.getStylesheets().add("/org/kordamp/bootstrapfx/bootstrapfx.css");
//            scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto&display=swap");
            scene.getStylesheets().add("style.css");
            myStage.setTitle("Gestion des locations");
            myStage.setScene(scene);
            myStage.show();
        } else infoLabel.setText("Nom d'utilisateur ou mot de passe valide(s).");
    }

    public void onEnter(ActionEvent event) throws SQLException, IOException, InterruptedException {
        LoginCheck(event);
    }
}