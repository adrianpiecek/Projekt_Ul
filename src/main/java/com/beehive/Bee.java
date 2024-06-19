package com.beehive;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.concurrent.Semaphore;
import java.util.random.RandomGenerator;

public class Bee extends Thread {
    private int maxVisits; // Maksymalna liczba odwiedzin w ulu przed śmiercią
    private int hiveCapacity; // Pojemność ula
    private static Semaphore entranceSemaphore = new Semaphore(1); // Semafor dla wejścia do ula

    private int visits = 0;
    private final Hive hive;
    private final double startX, startY;
    private final ImageView beeImageView;

    public Bee(Hive hive, int maxVisits, int hiveCapacity, ImageView beeImageView,double startX, double startY) {
        this.hive = hive;
        this.maxVisits = maxVisits;
        this.hiveCapacity = hiveCapacity;
        this.beeImageView = beeImageView;
        this.startX = startX;
        this.startY = startY;
    }

    private void animateBeeToHive() {
        Platform.runLater(() -> {
            double hiveX = 460;
            double hiveY = 200;

            Path pathToHive = new Path();
            pathToHive.getElements().add(new MoveTo(startX, startY));
            pathToHive.getElements().add(new MoveTo(hiveX, hiveY));

            PathTransition pathTransitionToHive = new PathTransition();
            pathTransitionToHive.setDuration(javafx.util.Duration.seconds(2));
            pathTransitionToHive.setPath(pathToHive);
            pathTransitionToHive.setNode(beeImageView);
            pathTransitionToHive.play();
        });
    }

    private void animateBeeFromHive() {
        Platform.runLater(() -> {
            Path pathFromHive = new Path();
            pathFromHive.getElements().add(new MoveTo(460, 200));
            pathFromHive.getElements().add(new MoveTo(startX, startY));

            PathTransition pathTransitionFromHive = new PathTransition();
            pathTransitionFromHive.setDuration(javafx.util.Duration.seconds(2));
            pathTransitionFromHive.setPath(pathFromHive);
            pathTransitionFromHive.setNode(beeImageView);
            pathTransitionFromHive.play();
        });
    }

    public void showImage(){
        beeImageView.setImage(new Image("file:resources/images/bee.png"));

    }

    @Override
    public void run() {
        try {
            while (visits < maxVisits) {
                if(hive.getHiveBeesCount() >= hiveCapacity) {
                    System.out.println("Pszczola " + this.getId() + " nie moze wejsc do ula, bo jest pelny.");
                    //czekaj losowy czas
                    Thread.sleep(RandomGenerator.getDefault().nextInt(1000, 5000));
                    continue;
                }
                Platform.runLater(this::animateBeeToHive);
                entranceSemaphore.acquire(); // Wejście do ula
                hive.enterHive(this);
                entranceSemaphore.release(); // Zwolnienie wejścia
                // Pszczoła przebywa w ulu
                Thread.sleep(2000); // Symulacja przebywania w ulu
                hive.exitHive(this);
                Platform.runLater(this::animateBeeFromHive);
                visits++;
                // Pszczoła przebywa poza ulem
                Thread.sleep(2000); // Symulacja przebywania poza ulem
            }
            System.out.println("Pszczola " + this.getId() + " umiera.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
