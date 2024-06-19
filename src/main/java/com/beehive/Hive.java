package com.beehive;

import java.util.concurrent.Semaphore;

import static java.lang.Math.floor;

public class Hive {
    private static int hiveCapacity; // Maksymalna liczba pszczół w ulu
    private final Semaphore hiveSemaphore = new Semaphore(hiveCapacity);
//    private AtomicInteger hiveBeesCount = new AtomicInteger(0); // Liczba pszczół w ulu
//    private AtomicInteger totalBeesCount = new AtomicInteger(0); // Liczba pszczół łącznie
    private int hiveBeesCount = 0;
    private int totalBeesCount = 0;

    public Hive(int initialBees) {
        hiveCapacity = (int)(floor((double) initialBees /2));
    }
    public void enterHive(Bee bee) throws InterruptedException {
        hiveSemaphore.acquire();
//        hiveBeesCount.incrementAndGet();
        hiveBeesCount++;
        System.out.println("Bee " + bee.getId() + " entered the hive. Bees inside: " + hiveBeesCount);

    }

    public void exitHive(Bee bee) {
        //hiveBeesCount.decrementAndGet();
        hiveBeesCount--;
        hiveSemaphore.release();
        System.out.println("Bee " + bee.getId() + " exited the hive. Bees inside: " + hiveBeesCount);
    }

    public int getHiveBeesCount() {
        return hiveBeesCount;
    }
    public int getTotalBeesCount() {
        return totalBeesCount;
    }
    public int getHiveCapacity() {
        return hiveCapacity;
    }

    public void layEggs() throws InterruptedException {
        if (hiveBeesCount < hiveCapacity / 2) {
            System.out.println("Królowa składa jaja...");
            // Symulacja składania jaj
            Thread.sleep(4000);
            System.out.println("Jajo złożone.");
            //this.totalBeesCount.incrementAndGet();
            this.totalBeesCount++;
            this.hiveBeesCount++;
            System.out.println("Liczba pszczół w ulu: " + hiveBeesCount + " / " + hiveCapacity);
        } else {
            System.out.println("Nie ma miejsca na jaja.");
        }
    }
}

