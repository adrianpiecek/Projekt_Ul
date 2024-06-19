package com.beehive;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BeeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BeeApplication.class.getResource("beehive_scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bee Simulator");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public Button StartButton;
    public void StartSimulation() {

    }

    public static void main(String[] args) {
        launch();
    }
}