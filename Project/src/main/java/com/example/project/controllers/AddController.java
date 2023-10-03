package com.example.project.controllers;/*
 * @created 03/10/2023 - 5:26 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Le champ pour l'ID est desactive car la base de donnees gere les identifiants (auto-incrementation) :
        idField.getStyleClass().add("hidden");
        idField.setEditable(false);
    }
}
