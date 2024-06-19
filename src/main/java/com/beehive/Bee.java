package com.beehive;

import java.util.concurrent.Semaphore;

public class Bee extends Thread {
    private int maxVisits = 5; // Maksymalna liczba odwiedzin w ulu przed śmiercią
    private int hiveCapacity = 100; // Pojemność ula
    private static Semaphore entranceSemaphore = new Semaphore(1); // Semafor dla wejścia do ula

    private int visits = 0;
    private Hive hive;

    public Bee(Hive hive, int maxVisits, int hiveCapacity) {
        this.hive = hive;
        this.maxVisits = maxVisits;
        this.hiveCapacity = hiveCapacity;
    }

    @Override
    public void run() {
        try {
            while (visits < maxVisits) {
                entranceSemaphore.acquire(); // Wejście do ula
                hive.enterHive(this);
                entranceSemaphore.release(); // Zwolnienie wejścia
                // Pszczoła przebywa w ulu
                Thread.sleep(1000); // Symulacja przebywania w ulu
                hive.exitHive(this);
                visits++;
                // Pszczoła przebywa poza ulem
                Thread.sleep(2000); // Symulacja przebywania poza ulem
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
