package com.mlamp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步队列和阻塞队列
 */
public class LockCondition {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition conditionA = lock.newCondition();
    public static Condition conditionB = lock.newCondition();
    public static Condition conditionC = lock.newCondition();

    public static void main(String[] args) {
        Thread threadA = new ThreadA();
        Thread threadB = new ThreadB();
        Thread threadC = new ThreadC();
        threadA.start();
        threadB.start();
        threadC.start();
    }

    public static long index = 0;

    private static class ThreadA extends Thread {
        private int count = 10;

        @Override
        public void run() {
            super.run();
            try {
                while (count-- != 0) {
                    lock.lock();
                    System.out.println(" thread-a " + index++);
                    conditionB.signal();
                    conditionA.await();
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private static class ThreadB extends Thread {
        private int count = 10;

        @Override
        public void run() {
            super.run();
            try {
                while (count-- != 0) {
                    lock.lock();
                    System.out.println(" thread-b " + index++);
                    conditionC.signal();
                    conditionB.await();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();

            } finally {
                lock.unlock();
            }
        }
    }

    private static class ThreadC extends Thread {
        private int count = 10;

        @Override
        public void run() {
            super.run();
            try {
                while (count-- != 0) {
                    lock.lock();
                    System.out.println(" thread-c " + index++);
                    conditionA.signal();
                    conditionC.await();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

}
