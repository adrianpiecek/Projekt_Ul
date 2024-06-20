package com.beehive;

import javafx.scene.image.ImageView;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

import static java.lang.Math.floor;

public class Hive {
    private static int hiveCapacity; // Maksymalna liczba pszczół w ulu
    private static Semaphore hiveSemaphore;
    private final AtomicInteger hiveBeesCount = new AtomicInteger(0); // Liczba pszczół w ulu
    private final AtomicInteger totalBeesCount = new AtomicInteger(0); // Liczba pszczół łącznie
    private final int maxVisits;

    public Hive(int initialBees,int maxVisits){
        hiveCapacity = (int)(floor((double) initialBees /2))-1;
        hiveSemaphore = new Semaphore(hiveCapacity, true);
        this.maxVisits = maxVisits;
    }
    public void enterHive(Bee bee) throws InterruptedException {
        hiveSemaphore.acquire();
        hiveBeesCount.incrementAndGet();
        System.out.println("Bee " + bee.getId() + " entered the hive. Bees inside: " + hiveBeesCount);

    }
    public void exitHive(Bee bee) {
        hiveBeesCount.decrementAndGet();
        System.out.println("Bee " + bee.getId() + " exited the hive. Bees inside: " + hiveBeesCount);
        hiveSemaphore.release();
    }


    public int incrementTotalBeesCount() {
        return totalBeesCount.incrementAndGet();
    }
    public int getHiveBeesCount() {
        return hiveBeesCount.get();
    }
    public int getTotalBeesCount() {
        return totalBeesCount.get();
    }
    public int getHiveCapacity() {
        return hiveCapacity;
    }

    public void layEggs() throws InterruptedException {
        if (hiveBeesCount.get() < hiveCapacity) {
            // Symulacja składania jaj
            System.out.println("Królowa składa jaja...");
            Thread.sleep(RandomGenerator.getDefault().nextInt(2500*maxVisits,2500*maxVisits+4000));
            hiveSemaphore.acquire();
            this.totalBeesCount.incrementAndGet();
            this.hiveBeesCount.incrementAndGet();
            System.out.println("Jajo złożone.");
            System.out.println("Liczba pszczół w ulu: " + hiveBeesCount + " / " + hiveCapacity);
        } else {
            System.out.println("Nie ma miejsca na jaja.");
        }
    }
}

