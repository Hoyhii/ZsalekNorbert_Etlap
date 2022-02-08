package com.example.etlap.controllers;

import com.example.etlap.EtlapApplication;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Controller {
    protected Stage stage;

    public Stage getStage() {
        return stage;
    }

    protected void alert(String string) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText(string);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    protected void hibaKiir(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hiba!");
        alert.setHeaderText(e.getClass().toString());
        alert.setContentText(e.getMessage());
        Timer alertTimer = new Timer();
        alertTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> alert.show());
            }
        }, 500);
    }

    public static Controller ujablak(String fxml, String title, int width, int height) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(EtlapApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.setScene(scene);
        Controller controller = fxmlLoader.getController();
        controller.stage = stage;
        return controller;
    }

    protected boolean confirm(String uzenet){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Biztos?");
        alert.setHeaderText(uzenet);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
