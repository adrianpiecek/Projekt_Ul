package com.beehive;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Hive {
    private static final int MAX_BEES_INSIDE = 10; // Maksymalna liczba pszczół w ulu
    private Semaphore hiveSemaphore = new Semaphore(MAX_BEES_INSIDE);
    private AtomicInteger beeCount = new AtomicInteger(0);

    public void enterHive(Bee bee) throws InterruptedException {
        hiveSemaphore.acquire();
        beeCount.incrementAndGet();
        System.out.println("Bee " + bee.getId() + " entered the hive. Bees inside: " + beeCount.get());
    }

    public void exitHive(Bee bee) {
        beeCount.decrementAndGet();
        hiveSemaphore.release();
        System.out.println("Bee " + bee.getId() + " exited the hive. Bees inside: " + beeCount.get());
    }

    public void layEggs() throws InterruptedException {
        if (beeCount.get() < MAX_BEES_INSIDE / 2) {
            System.out.println("Queen Bee is laying eggs...");
            // Symulacja składania jaj
            Thread.sleep(2000);
            System.out.println("Eggs laid.");
        } else {
            System.out.println("Not enough space to lay eggs.");
        }
    }
}

