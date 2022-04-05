package com.company;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Task {

    /*for random number (used for sleeping thread a random duration)*/
    private Random random = new Random();

    /*determines whether the ferry is on the island or mainland*/
    private Semaphore ferry = new Semaphore(1);

    /*max capacity of the parking lot on the island*/
    private static int island_parking_lot_max_capacity = 10;

    /*max capacity of the ferry*/
    private static int ferry_max_capacity = 3;

    /*vector containing list of current cars on the island parking lot*/
    private Vector<Integer> island_parking_lot_current = new Vector<>();

    /*iterator for island_parking_lot_current*/
    private Iterator<Integer> island_parking_lot_current_it = island_parking_lot_current.iterator();

    /*vector containing list of current cars on the ferry*/
    private Vector<Integer> ferry_current = new Vector<>();

    /*iterator for ferry_current_it*/
    private Iterator<Integer> ferry_current_it = ferry_current.iterator();

    /*vector containing list of current cars waiting on the mainland*/
    private Vector<Integer> mainland_current = new Vector<>();

    /*iterator for main_current*/
    private Iterator<Integer> mainland_current_it = mainland_current.iterator();

    /*current ID count of cars*/
    int carId = 0;

    Task() {
        //start mainland with cars already there
        mainland_current.add(carId);
        carId++;
        mainland_current.add(carId);
        carId++;
        mainland_current.add(carId);
        carId++;
        mainland_current.add(carId);
        carId++;

        //start island with cars already there
        island_parking_lot_current.add(carId);
        carId++;
        island_parking_lot_current.add(carId);
        carId++;
        island_parking_lot_current.add(carId);
        carId++;
    }

    /*
    * Adds a car to the vector at random intervals
    */
    public void mainlandbg() throws InterruptedException {

        while (true) {
            //sleep for random amount of ms from 400 to 3000
            Thread.sleep(random.nextInt(3000 + 1 - 400) + 400);
            mainland_current.add(carId);
            System.out.println("carId: " + carId + " is now waiting on the mainland.");
            carId++;
        }

    }

    public void mainland() throws InterruptedException {

        /*specifies which car is being moved from mainland to ferry and vice versa*/
        int currentCarId;

        while (true) {

            ferry.acquire();
            System.out.println("Ferry arrives at the mainland.");
            Thread.sleep(1000);

            //if ferry is empty
            if (ferry_current.isEmpty()) {

                //move cars that are waiting on the mainland to the ferry until it reaches capacity/will reach island parking lot max capacity/no more cars
                while (ferry_current.size() <= ferry_max_capacity && !mainland_current.isEmpty() && island_parking_lot_current.size() + ferry_current.size() <= island_parking_lot_max_capacity) {
                    currentCarId = mainland_current.firstElement();
                    mainland_current.remove(Integer.valueOf(currentCarId)); //Integer.valueOf() grabs the actual value of currentCarId so remove works properly, otherwise remove will use another overloaded version of remove() or fail to find the correct element to remove
                    ferry_current.add(currentCarId);

                    System.out.println("carId: " + currentCarId + " boards the ferry from the mainland.");
                    Thread.sleep(1000);
                }

            } else {

                //move all cars to the mainland
                ferry_current.clear();
                System.out.println("All the cars leave the ferry and enter the mainland.");

                //move cars that are waiting on the mainland to the ferry until it reaches capacity/will reach island parking lot max capacity/no more cars
                while (ferry_current.size() <= ferry_max_capacity && !mainland_current.isEmpty() && island_parking_lot_current.size() + ferry_current.size() <= island_parking_lot_max_capacity) {
                    currentCarId = mainland_current.firstElement();
                    mainland_current.remove(Integer.valueOf(currentCarId));
                    ferry_current.add(currentCarId);

                    System.out.println("carId: " + currentCarId + " boards the ferry from the mainland.");
                    Thread.sleep(1000);
                }

            }

            ferry.release();
            System.out.println("Ferry leaves for the island.\n");
            Thread.sleep(5000);

        }
    }

    public void island() throws InterruptedException {

        int currentCarId;

        while (true) {

            ferry.acquire();
            System.out.println("Ferry arrives at the island");

            //if ferry is empty
            if (ferry_current.isEmpty()) {

                //move cars that are waiting on the island to the ferry until it reaches capacity or island parking lot is empty
                while (ferry_current.size() <= ferry_max_capacity && !island_parking_lot_current.isEmpty()) {
                    currentCarId = island_parking_lot_current.firstElement();
                    island_parking_lot_current.remove(Integer.valueOf(currentCarId));
                    ferry_current.add(currentCarId);

                    System.out.println("carId: " + currentCarId + " boards the ferry from the island.");
                    Thread.sleep(1000);
                }

            } else {

                //move all cars from ferry to island parking lot
                ferry_current_it = ferry_current.iterator();
                while (ferry_current_it.hasNext()) {
                    currentCarId = ferry_current.remove(ferry_current.size() - 1);
                    island_parking_lot_current.add(currentCarId);
                    System.out.println("carId: " + currentCarId + " leaves the ferry and enters the island.");
                    Thread.sleep(1000);
                }

                //move cars that are waiting on the island to the ferry until it reaches capacity
                while (ferry_current.size() <= ferry_max_capacity && !island_parking_lot_current.isEmpty()) {
                    currentCarId = island_parking_lot_current.firstElement();
                    island_parking_lot_current.remove(Integer.valueOf(currentCarId));
                    ferry_current.add(currentCarId);

                    System.out.println("carId: " + currentCarId + " boards the ferry from the island.");
                    Thread.sleep(1000);
                }

            }

            ferry.release();
            System.out.println("Ferry leaves for the mainland.\n");
            Thread.sleep(5000);

        }

    }

}
