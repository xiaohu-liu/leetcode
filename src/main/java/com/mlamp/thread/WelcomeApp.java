package com.mlamp.thread;

public class WelcomeApp  {
    public static void main(String[] args) {
        System.out.printf("1. Welcome , i am %s. %n",Thread.currentThread().getName());
        Thread welcomeThread = new WelcomeThread();
        welcomeThread.start();
        new Thread(new WelcomeTask()).start();
    }

    private static final class WelcomeTask implements Runnable{
        @Override
        public void run() {
            System.out.printf("3.Welcome I am %s.%n",Thread.currentThread().getName());
        }
    }
}
