package com.example.project.controllers;/*
 * @created 03/10/2023 - 5:26 p.m.
 * @project Env_Graph_Session_Project
 * @author Emilie Echevin
 */

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddController {

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
}
