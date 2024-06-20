package com.beehive;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

import static com.beehive.BeeApplication.hiveX;
import static com.beehive.BeeApplication.hiveY;

public class Bee extends Thread {
    private final int maxVisits; // Maksymalna liczba odwiedzin w ulu przed śmiercią
    private final int hiveCapacity; // Pojemność ula

    private int visits = 0;
    private final Hive hive;
    private double startX, startY;
    private final ImageView beeImageView;

    public Bee(Hive hive, int maxVisits, ImageView beeImageView,double startX, double startY){
        this.hive = hive;
        this.maxVisits = maxVisits;
        this.hiveCapacity = hive.getHiveCapacity();
        this.beeImageView = beeImageView;
        this.startX = startX;
        this.startY = startY;
    }

    private void fly(double fromX, double fromY, double toX, double toY){
        Path hivePath = new Path();
        hivePath.getElements().add(new MoveTo(fromX, fromY));
        hivePath.getElements().add(new LineTo(toX, toY));

        PathTransition hivePathTransition = new PathTransition();
        hivePathTransition.setDuration(Duration.seconds(1));
        hivePathTransition.setPath(hivePath);
        hivePathTransition.setNode(beeImageView);
        hivePathTransition.play();
    }



    @Override
    public void run() {
        try {
            while (visits < maxVisits) {
                hive.enterHive(this);
                Platform.runLater(() -> fly(startX, startY, hiveX, hiveY));
                Thread.sleep(RandomGenerator.getDefault().nextInt(1000,2000)); // Symulacja przebywania w ulu
                hive.exitHive(this);
                int randomX = RandomGenerator.getDefault().nextInt(0, (int)hiveX - 100);
                int randomY = RandomGenerator.getDefault().nextInt(120, 360);
                Platform.runLater(() -> fly(hiveX, hiveY, randomX, randomY));
                this.startX= randomX;
                this.startY= randomY;
                visits++;
                Thread.sleep(RandomGenerator.getDefault().nextInt(3000,5000)); // Symulacja przebywania poza ulem
            }
            //zakończenie procesu
            System.out.println("Pszczola " + this.getId() + " umiera.");
            beeImageView.visibleProperty().set(false);
            hive.decrementTotalBeesCount();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}