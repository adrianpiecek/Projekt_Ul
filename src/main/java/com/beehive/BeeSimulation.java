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

    public BeeSimulation(int initialBees, int maxVisits, List<ImageView> beeImageViews) {
        this.executor = Executors.newFixedThreadPool(initialBees + 1); // +1 dla królowej
        this.maxVisits = maxVisits;
        this.hive = new Hive(initialBees, maxVisits, this);
        this.maxHiveCapacity = hive.getHiveCapacity();
        this.initialBees = initialBees;
        this.queenBee = new QueenBee(hive);
        this.beeImageViews = beeImageViews;
    }

    public void start() {
        executor.execute(queenBee);
    }

    public void createNewBee(ImageView beeImageView, double startX, double startY) {
        Bee newBee = new Bee(hive, maxVisits, beeImageView, startX, startY);
        executor.execute(newBee);
        hive.incrementTotalBeesCount();
        System.out.println("Bee " + newBee.getId()  + " created");
    }

    public Hive getHive() {
        return hive;
    }

    public void stop() {
        executor.shutdownNow();
    }

}
