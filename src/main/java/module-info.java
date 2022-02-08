module com.example.etlap {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.example.etlap to javafx.fxml;
    exports com.example.etlap;
    exports com.example.etlap.controllers;
    opens com.example.etlap.controllers to javafx.fxml;
}