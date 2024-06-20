package com.beehive;

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
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

