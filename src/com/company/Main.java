package com.company;

import javax.sound.midi.SysexMessage;

public class Main {

    public static void main(String[] args) {

        /*Thread t2 = new Thread(ship);
        t1.setName("Thread 1");
        t2.setName("Thread 2");
        t1.start();
        t1.join();
        t2.start();*/

        final Task service = new Task();

        Thread mainlandbg = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    service.mainlandbg();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread mainland = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    service.mainland();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread island = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    service.island();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mainlandbg.start();
        mainland.start();
        island.start();

    }
}

/*
* Case 1: Island to Mainland
*   -
*
*
* Case 2: Mainland to Island
*/
