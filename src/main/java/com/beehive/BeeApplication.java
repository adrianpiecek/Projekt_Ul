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

import static com.beehive.Hive.hiveSemaphore;

public class BeeApplication extends Application{
    static Image beeImage = new Image(String.valueOf(BeeApplication.class.getResource("bee.png")));
    static List<ImageView> beeImageViews = new ArrayList<>();
    static double hiveX;
    static double hiveY;

    private BeeSimulation simulation;
    private ScheduledExecutorService scheduler;

    @FXML
    public TextField InitialBeeCountTextField;
    @FXML
    public TextField MaxVisitsTextField;
    @FXML
    public Label BeeCountLabel;
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
        stage.getIcons().add(beeImage);
        stage.resizableProperty().setValue(Boolean.FALSE);
    }

    public void StartSimulation() {
        int InitialBees = Integer.parseInt(InitialBeeCountTextField.getText());
        int maxVisits = Integer.parseInt(MaxVisitsTextField.getText());

        hiveX = HiveImageView.getLayoutX() + 50;
        hiveY = HiveImageView.getLayoutY();
        simulation = new BeeSimulation(InitialBees, maxVisits);

        //załadowanie obrazków pszczół i stworzenie ich
        for (int i = 0; i < InitialBees; i++) {
            createAndAddNewBee();
        }
        simulation.start();

        scheduler = Executors.newScheduledThreadPool(6);
        scheduler.scheduleAtFixedRate(this::updateHiveStatus, 0, 1, TimeUnit.SECONDS);
    }

    public void createAndAddNewBee() {
        ImageView newBeeImageView = new ImageView(beeImage);
        double startX = RandomGenerator.getDefault().nextInt(0, (int)hiveX- 100);
        double startY = RandomGenerator.getDefault().nextInt(120, 360);
        newBeeImageView.setFitHeight(30);
        newBeeImageView.setFitWidth(30);
        newBeeImageView.setX(startX);
        newBeeImageView.setY(startY);
        simulation.createNewBee(newBeeImageView, startX, startY);
        beeImageViews.add(newBeeImageView);
        AnchorPane.getChildren().add(newBeeImageView);
    }

    private void updateHiveStatus() {
        Platform.runLater(() -> {
            Hive hive = simulation.getHive();
            BeeCountLabel.setText("Łączna ilość pszczół: " + hive.getTotalBeesCount() + "\nIlość pszczół w ulu: " + hive.getHiveBeesCount() + "/" + hive.getHiveCapacity());
            if(hive.getHatchedBees()>0){
                for(int i = 0; i < hive.getHatchedBees(); i++) {
                    createAndAddNewBee();
                    hive.decrementHiveBeesCount();
                    hive.decrementHatchedBees();
                }
                hiveSemaphore.release();
            }
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