package com.beehive;

import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeeSimulation {
    private final Hive hive;
    private final QueenBee queenBee;
    private final int maxVisits;
    private final ExecutorService executor;


    public BeeSimulation(int initialBees, int maxVisits) {
        this.executor = Executors.newFixedThreadPool(initialBees + 1); // +1 dla królowej
        this.maxVisits = maxVisits;
        this.hive = new Hive(initialBees, maxVisits, this);
        this.queenBee = new QueenBee(hive);

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
