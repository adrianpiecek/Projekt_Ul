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

    public BeeSimulation(int initialBees, int maxHiveCapacity, int maxVisits, List<ImageView> beeImageViews) {
        this.executor = Executors.newFixedThreadPool(initialBees + 1); // +1 dla królowej
        this.maxVisits = maxVisits;
        this.maxHiveCapacity = maxHiveCapacity;
        this.hive = new Hive(maxHiveCapacity);
        this.initialBees = initialBees;
        this.queenBee = new QueenBee(hive);
        this.beeImageViews = beeImageViews;
    }

    public void start() {
        for (int i = 0; i < initialBees; i++) {
            ImageView beeImageView = beeImageViews.get(i);
            executor.execute(new Bee(hive, maxVisits, maxHiveCapacity, beeImageView, beeImageView.getX(), beeImageView.getY()));
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
