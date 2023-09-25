package com.example.project.controllers;/*
 * @created 24/09/2023 - 5:11 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class LocationController implements EventHandler<ActionEvent>, Initializable {

    @FXML
    public Button addBtn;

    @FXML
    public Button saveBtn;

    @FXML
    public Button modifBtn;

    @FXML
    public Button delBtn;

    private Location model;

//    public LocationController;

    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Seul le bouton Ajouter sera visible au demarrage de la page:
        saveBtn.setVisible(false);
        modifBtn.setVisible(false);
        delBtn.setVisible(false);
    }
}
