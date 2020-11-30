package com.mlamp.thread;

public class WelcomeThread extends Thread {

    @Override
    public void run() {
        System.out.printf("2.Welcome ! I am %s.%n", Thread.currentThread().getName());
    }
}
