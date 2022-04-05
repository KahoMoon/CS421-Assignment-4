package com.company;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Task {

    /*for random number*/
    Random random = new Random();

    /*determines whether the ferry is on the island or mainland*/
    Semaphore ferry = new Semaphore(1);

    /*max capacity of the parking lot on the island*/
    static int island_parking_lot_max_capacity = 10;

    /*max capacity of the ferry*/
    static int ferry_max_capacity = 4;

    /*vector containing list of current cars on the island parking lot*/
    Vector<Integer> island_parking_lot_current = new Vector<>();
    Iterator<Integer> island_parking_lot_current_it = island_parking_lot_current.iterator();

    /*vector containing list of current cars on the ferry*/
    Vector<Integer> ferry_current = new Vector<>();
    Iterator<Integer> ferry_current_it = ferry_current.iterator();

    /*vector containing list of current cars waiting on the mainland*/
    Vector<Integer> mainland_current = new Vector<>();
    Iterator<Integer> mainland_current_it = mainland_current.iterator();

    /*current ID count of cars*/
    int carId = 0;

    Integer firstElement;

    /*
    * Adds a car to the vector at random intervals
    */
    public void mainlandbg() throws InterruptedException {

        while (true) {
            //sleep for random amount of ms from 400 - 3000
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
            System.out.println("Ferry arrives at the mainland.");

            //if ferry is empty
            if (ferry_current.isEmpty()) {

                //move cars that are waiting on the mainland to the ferry until it reaches capacity/will fill island parking lot
                while (ferry_current.size() <= ferry_max_capacity && !mainland_current.isEmpty() && island_parking_lot_current.size() + ferry_current.size() <= island_parking_lot_max_capacity) {
                    currentCarId = mainland_current.remove(mainland_current.size() - 1);    //instead of removing from end, remove from start if you want more realism
                    ferry_current.add(currentCarId);

                    System.out.println(currentCarId + " boards the ferry.");
                }

            } else {

                //move all cars to the mainland
                ferry_current.clear();
                System.out.println("All the cars leave the ferry and enter the mainland.");

                //move cars that are waiting on the mainland to the ferry until it reaches capacity
                while (ferry_current.size() <= ferry_max_capacity && !mainland_current.isEmpty() && island_parking_lot_current.size() + ferry_current.size() <= island_parking_lot_max_capacity) {

                    currentCarId = mainland_current.remove(mainland_current.size() - 1);    //instead of removing from end, remove from start if you want more realism
                    ferry_current.add(currentCarId);
                }

            }

            ferry.release();
            System.out.println("Ferry leaves for the island.");
            wait(9000);

        }
    }

    public void island() throws InterruptedException {

        int currentCarId;

        while (true) {

            ferry.acquire();;
            System.out.println("Ferry arrives at the island");

            //if ferry is empty
            if (ferry_current.isEmpty()) {

                //move cars that are waiting on the island to the ferry until it reaches capacity
                while (ferry_current.size() <= ferry_max_capacity && !island_parking_lot_current.isEmpty()) {
                    currentCarId = island_parking_lot_current.remove(island_parking_lot_current.size() - 1);
                    ferry_current.add(currentCarId);

                    System.out.println(currentCarId + " boards the ferry.");
                }

            } else {

                while (ferry_current_it.hasNext()) {
                    currentCarId = ferry_current.remove(ferry_current.size() - 1);
                    island_parking_lot_current.add(currentCarId);
                    System.out.println(currentCarId + " leaves the ferry and enters the island.");
                }

                while (ferry_current.size() <= ferry_max_capacity && !island_parking_lot_current.isEmpty()) {

                }

            }

        }

    }

}
