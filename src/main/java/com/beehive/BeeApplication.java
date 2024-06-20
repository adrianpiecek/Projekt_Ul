package com.beehive;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.random.RandomGenerator;
import java.io.IOException;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BeeApplication extends Application {
    Image beeImage = new Image(String.valueOf(getClass().getResource("bartek1.png")));
    private final List<ImageView> beeImageViews = new ArrayList<>();
    private double hiveX;
    private double hiveY;

    private BeeSimulation simulation;
    private ScheduledExecutorService scheduler;

    @FXML
    public TextField InitialBeeCountTextField;
    @FXML
    public TextField MaxVisitsTextField;
    @FXML
    public Label BeeCountLabel;
    @FXML
    public Label HiveCountLabel;
    @FXML
    public Button StartButton;
    @FXML
    public ImageView HiveImageView;
    @FXML
    public AnchorPane AnchorPane;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BeeApplication.class.getResource("beehive_scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bee Simulator");
        stage.setScene(scene);
        stage.show();
    }

    public void StartSimulation() {
        int InitialBees = Integer.parseInt(InitialBeeCountTextField.getText());
        int maxVisits = Integer.parseInt(MaxVisitsTextField.getText());

        hiveX = HiveImageView.getLayoutX();
        hiveY = HiveImageView.getLayoutY();

        //załadowanie obrazków pszczół
        for (int i = 0; i < InitialBees; i++) {
            ImageView newBeeImageView = new ImageView(beeImage);
            double StartX = RandomGenerator.getDefault().nextInt(0, (int)hiveX- 100);
            double StartY = RandomGenerator.getDefault().nextInt(120, 360);
            newBeeImageView.setFitHeight(30);
            newBeeImageView.setFitWidth(30);
            newBeeImageView.setX(StartX);
            newBeeImageView.setY(StartY);
            beeImageViews.add(newBeeImageView);
            AnchorPane.getChildren().add(newBeeImageView);
            //newBee();
        }
        simulation = new BeeSimulation(InitialBees, maxVisits, beeImageViews,hiveX, hiveY);
        simulation.start();

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateHiveStatus, 0, 1, TimeUnit.SECONDS);
    }

    private void updateHiveStatus() {
        Platform.runLater(() -> {
            Hive hive = simulation.getHive();
            HiveCountLabel.setText("Ilość pszczół w ulu: " + hive.getHiveBeesCount() + "/" + hive.getHiveCapacity());
            BeeCountLabel.setText("Łączna ilość pszczół: " + hive.getTotalBeesCount());
        });
    }

    @Override
    public void stop() {
        if(scheduler != null) {
            scheduler.shutdownNow();
        }
        if(simulation != null) {
            simulation.stop();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}