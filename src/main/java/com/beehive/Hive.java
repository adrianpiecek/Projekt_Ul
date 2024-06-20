package com.beehive;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

import static java.lang.Math.*;

public class Hive {
    private static int hiveCapacity; // Maksymalna liczba pszczół w ulu
    static Semaphore hiveSemaphore;
    private static Semaphore entrance1Semaphore;
    private static Semaphore entrance2Semaphore;
    private final AtomicInteger hiveBeesCount = new AtomicInteger(0); // Liczba pszczół w ulu
    private final AtomicInteger totalBeesCount = new AtomicInteger(0); // Liczba pszczół łącznie
    private final AtomicInteger hatchedBees = new AtomicInteger(0); // Liczba wyklutych pszczół
    private final int maxVisits;
    private final BeeSimulation simulation;
    private final AtomicInteger nextEntrance = new AtomicInteger(0); // Do śledzenia następnego wejścia


    public Hive(int initialBees,int maxVisits, BeeSimulation simulation){
        hiveCapacity = (int)(floor((double) initialBees /2))-1;
        hiveSemaphore = new Semaphore(hiveCapacity, true);
        entrance1Semaphore = new Semaphore(1, true);
        entrance2Semaphore = new Semaphore(1, true);
        this.maxVisits = maxVisits;
        this.simulation = simulation;
    }
    public int enterHive(Bee bee) throws InterruptedException {
        int entranceNumber = 0;
        Semaphore chosenEntrance = chooseEntrance();
        if (chosenEntrance == entrance1Semaphore) {
            System.out.println("Bee " + bee.getId() + " waiting at entrance 1");
            entranceNumber =1;
        } else {
            System.out.println("Bee " + bee.getId() + " waiting at entrance 2");
            entranceNumber =2;
        }
        hiveSemaphore.acquire(); // zablokuj miejsce w ulu
        while(!chosenEntrance.tryAcquire(150, TimeUnit.MILLISECONDS)){
            bee.sleep(RandomGenerator.getDefault().nextInt(50,250));
            System.out.println("Bee " + bee.getId() + " jeszcze raz probuje wejsc " + entranceNumber);
        } // Zablokuj dostęp do wybranego wejścia
        hiveBeesCount.incrementAndGet();
        System.out.println("Bee " + bee.getId() + " entered the hive. Bees inside: " + hiveBeesCount + "/" + hiveCapacity);
        chosenEntrance.release();
        return entranceNumber;
    }
    public void exitHive(Bee bee) throws InterruptedException {
        Semaphore chosenEntrance = chooseEntrance();
        System.out.println("Bee " + bee.getId() + " exiting the hive...");
        while(!chosenEntrance.tryAcquire(700, TimeUnit.MILLISECONDS)){
            bee.sleep(RandomGenerator.getDefault().nextInt(50,250));
            System.out.println("Bee " + bee.getId() + " jeszcze raz probuje wyjsc");
        }
        hiveBeesCount.decrementAndGet();
        System.out.println("Bee " + bee.getId() + " exited the hive. Bees inside: " + hiveBeesCount);
        hiveSemaphore.release();
        chosenEntrance.release();
    }

    private Semaphore chooseEntrance() {
        // Round-robin selection
        int entrance = nextEntrance.getAndUpdate(n -> (n + 1) % 2);
        return entrance == 0 ? entrance1Semaphore : entrance2Semaphore;
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

    public static Semaphore getHiveSemaphore(){
        return hiveSemaphore;
    }

    public void layEggs() throws InterruptedException {
            while(true){
                try {
                    if(!hiveSemaphore.tryAcquire(150, TimeUnit.MILLISECONDS)){
                        System.out.println("Królowa czeka na miejsce w ulu...");
                        Thread.sleep(RandomGenerator.getDefault().nextInt(50,150));
                        continue;
                    }
                    else{
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        if (hiveBeesCount.get() < hiveCapacity) {
            // Symulacja składania jaj
            System.out.println("Królowa składa jaja...");
            Thread.sleep(RandomGenerator.getDefault().nextInt(300*(totalBeesCount.get()/hiveCapacity),300*(totalBeesCount.get()/hiveCapacity)+20));
            this.hiveBeesCount.incrementAndGet();
            System.out.println("Jajo złożone.");
            Thread.sleep(RandomGenerator.getDefault().nextInt(5*totalBeesCount.get()+1,5*totalBeesCount.get()+40));
            this.hatchedBees.incrementAndGet();
            System.out.println(hatchedBees.get() + " Jaj gotowe do wyklucia. Liczba pszczół w ulu: " + hiveBeesCount + " / " + hiveCapacity);
        }else{
            hiveSemaphore.release();
        }
    }
}

