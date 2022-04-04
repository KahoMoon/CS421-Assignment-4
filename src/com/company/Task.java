package com.company;

import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Task {

    Random random = new Random();

    Semaphore ferry = new Semaphore(1);
    static int island_parking_lot_max_capacity = 10;
    static int ferry_max_capacity = 4;
    Vector<Integer> island_parking_lot_current = new Vector<>();
    Vector<Integer> ferry_current = new Vector<>();
    Vector<Integer> mainland_current = new Vector<>();
    int carId = 0;

    /*
    * Adds a car to the vector at random intervals
    */
    public void mainlandbg() throws InterruptedException {

        while (true) {
            Thread.sleep(random.nextInt(3000 + 1 - 400) + 400);
            mainland_current.add(carId);
            System.out.println("carId: " + carId + " is now waiting on the mainland.");
            carId++;
        }

    }

    public void mainland() throws InterruptedException {

        int currentCarId;

        while (true) {

            ferry.acquire();

            //if ferry is empty
            if (ferry_current.size() == 0) {

                //move cars that are waiting on the mainland to the ferry until it reaches capacity
                while (ferry_current.size() <= ferry_max_capacity && !mainland_current.isEmpty()) {
                    currentCarId = mainland_current.remove(mainland_current.size() - 1);    //instead of removing from end, remove from start if you want more realism
                    ferry_current.add(currentCarId);
                }

            } else {

                ferry_current.clear();

            }

            ferry.release();
            wait(9000);

        }
    }

    public void island() throws InterruptedException {

        int carNum  = 0;



    }

}
