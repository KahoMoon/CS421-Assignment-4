package com.company;

import javax.sound.midi.SysexMessage;

public class Main {

    public static void main(String[] args) {

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