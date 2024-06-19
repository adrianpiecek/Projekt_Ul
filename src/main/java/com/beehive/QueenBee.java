package com.beehive;

import java.util.concurrent.Semaphore;

public class QueenBee extends Thread {
    private Hive hive;

    public QueenBee(Hive hive) {
        this.hive = hive;
    }

    @Override
    public void run() {
        try {
            while (true) {
                hive.layEggs();
                Thread.sleep(5000); // Symulacja czasu między składaniem jaj
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

