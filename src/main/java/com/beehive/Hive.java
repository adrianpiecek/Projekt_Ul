package com.beehive;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

import static java.lang.Math.floor;

public class Hive {
    private static int hiveCapacity; // Maksymalna liczba pszczół w ulu
    private static Semaphore hiveSemaphore;
    private final AtomicInteger hiveBeesCount = new AtomicInteger(0); // Liczba pszczół w ulu
    private final AtomicInteger totalBeesCount = new AtomicInteger(0); // Liczba pszczół łącznie
    private final AtomicInteger hatchedBees = new AtomicInteger(0); // Liczba wyklutych pszczół
    private final int maxVisits;
    private final BeeSimulation simulation;

    public Hive(int initialBees,int maxVisits, BeeSimulation simulation){
        hiveCapacity = (int)(floor((double) initialBees /2))-1;
        hiveSemaphore = new Semaphore(hiveCapacity, true);
        this.maxVisits = maxVisits;
        this.simulation = simulation;
    }
    public void enterHive(Bee bee) throws InterruptedException {
        hiveSemaphore.acquire();
        hiveBeesCount.incrementAndGet();
        System.out.println("Bee " + bee.getId() + " entered the hive. Bees inside: " + hiveBeesCount + "/" + hiveCapacity);
    }
    public void exitHive(Bee bee) {
        hiveBeesCount.decrementAndGet();
        System.out.println("Bee " + bee.getId() + " exited the hive. Bees inside: " + hiveBeesCount);
        hiveSemaphore.release();
    }


    public void incrementTotalBeesCount() {
        totalBeesCount.incrementAndGet();
    }
    public void decrementTotalBeesCount() {
        totalBeesCount.decrementAndGet();
    }
    public int getHiveBeesCount() {
        return hiveBeesCount.get();
    }
    public void decrementHiveBeesCount() {
        hiveBeesCount.decrementAndGet();
    }
    public int getTotalBeesCount() {
        return totalBeesCount.get();
    }
    public int getHiveCapacity() {
        return hiveCapacity;
    }
    public int getHatchedBees(){
        return hatchedBees.get();
    }
    public void decrementHatchedBees(){
        hatchedBees.decrementAndGet();
    }

    public void layEggs() throws InterruptedException {
        if (hiveBeesCount.get() < hiveCapacity) {
            // Symulacja składania jaj
            hiveSemaphore.acquire();
            System.out.println("Królowa składa jaja...");
            Thread.sleep(RandomGenerator.getDefault().nextInt(2000*maxVisits,2000*maxVisits+4000));
            this.hiveBeesCount.incrementAndGet();
            System.out.println("Jajo złożone.");
            Thread.sleep(RandomGenerator.getDefault().nextInt(2500,4000));
            this.hatchedBees.incrementAndGet();
            System.out.println(hatchedBees.get() + " Jaj gotowe do wyklucia. Liczba pszczół w ulu: " + hiveBeesCount + " / " + hiveCapacity);
            hiveSemaphore.release();
        }
    }
}

