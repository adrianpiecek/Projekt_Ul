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

import java.util.random.RandomGenerator;
import java.io.IOException;

import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BeeApplication extends Application {
    final Image beeImage = new Image("file:resources/images/bee.png");
    private final List<ImageView> beeImageViews = new ArrayList<>();
    //private AnchorPane root;


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
        //root = AnchorPane;
    }

    public void StartSimulation() {
        Hive hive = new Hive(parseInt(InitialBeeCountTextField.getText()));
        QueenBee queenBee = new QueenBee(hive);
        int InitialBees = Integer.parseInt(InitialBeeCountTextField.getText());
        int maxHiveCapacity = hive.getHiveCapacity();
        int maxVisits = Integer.parseInt(MaxVisitsTextField.getText());
        queenBee.start();

        for (int i = 0; i < InitialBees; i++) {
            ImageView newBeeImageView = new ImageView(beeImage);
            double StartX = RandomGenerator.getDefault().nextInt(0, 200);
            double StartY = RandomGenerator.getDefault().nextInt(120, 360);
            newBeeImageView.setTranslateX(StartX);
            newBeeImageView.setTranslateY(StartY);
            beeImageViews.add(newBeeImageView);
            AnchorPane.getChildren().add(newBeeImageView);

        }
        simulation = new BeeSimulation(InitialBees, maxHiveCapacity, maxVisits, beeImageViews);
        simulation.start();

        scheduler = Executors.newScheduledThreadPool(8);
        scheduler.scheduleAtFixedRate(this::updateHiveStatus, 0, 1, TimeUnit.SECONDS);
    }


    private void updateHiveStatus() {
        Platform.runLater(() -> {
            Hive hive = simulation.getHive();
            HiveCountLabel.setText("Ilość pszczół w ulu: " + hive.getHiveBeesCount());
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