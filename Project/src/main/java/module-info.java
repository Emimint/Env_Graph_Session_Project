module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;

    opens com.example.project to javafx.fxml;
    opens com.example.project.controllers to javafx.fxml;
    exports com.example.project;
    exports com.example.project.controllers;
    exports com.example.project.models;
}
