package com.mlamp.thread;

import java.util.concurrent.TimeUnit;

public class SimpleTimer {
    private static int count;

    public static void main(String[] args) {
        count = args.length == 1 ? Integer.valueOf(args[0]) : 5;
        int remaining;

        Thread daemonThreadIns = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("hello world");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        daemonThreadIns.setDaemon(true);
        daemonThreadIns.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
//                    if (!daemonThreadIns.isInterrupted()) {
//                        daemonThreadIns.interrupt();
//                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                System.out.println("jvm shut down");
            }
        }));

        while (true) {
            remaining = countDown();
            if (remaining == 0) {
                break;
            } else {
                System.out.println(" remaining " + remaining + " second(s)");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("done");
    }

    private static int countDown() {
        return count--;
    }


}
