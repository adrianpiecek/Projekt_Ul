package com.beehive;

import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeeSimulation {
    private final Hive hive;
    private final QueenBee queenBee;
    private final int maxVisits;
    private final int maxHiveCapacity;
    private final int initialBees;
    private final ExecutorService executor;
    private final List<ImageView> beeImageViews;
    private final double HiveX, HiveY;

    public BeeSimulation(int initialBees, int maxVisits, List<ImageView> beeImageViews,double HiveX, double HiveY) {
        this.executor = Executors.newFixedThreadPool(initialBees + 1); // +1 dla królowej
        this.maxVisits = maxVisits;
        this.hive = new Hive(initialBees, maxVisits);
        this.maxHiveCapacity = hive.getHiveCapacity();
        this.initialBees = initialBees;
        this.queenBee = new QueenBee(hive);
        this.beeImageViews = beeImageViews;
        this.HiveX = HiveX;
        this.HiveY = HiveY;
    }

    public void start() {
        for (int i = 0; i < initialBees; i++) {
            ImageView beeImageView = beeImageViews.get(i);
            Bee newBee;
            executor.execute(newBee = new Bee(hive, maxVisits, beeImageView, beeImageView.getX(), beeImageView.getY(), HiveX, HiveY));
            hive.incrementTotalBeesCount();
            System.out.println("Bee " + newBee.getId()  + " created");
            //createNewBee(beeImageView, beeImageView.getX(), beeImageView.getY(), HiveX, HiveY);
        }
        executor.execute(queenBee);
    }

    public Hive getHive() {
        return hive;
    }

    public void stop() {
        executor.shutdownNow();
    }

}
