package com.mlamp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 */
public class App {
    public static final Object clock = new Object();
    public static volatile boolean stop1 = false;
    public static volatile boolean stop2 = false;

    public static void main(String[] args) {
        System.out.println("Hello World!");


        char[] ch = new char[26];
        int[] number = new int[27];
        for (int index = 0; index < ch.length; index++) {
            ch[index] = (char) (65 + index);
            number[index] = index;
        }
        number[26] = 26;
        /**
         * object wait() & notify() implementation
         */
        //new PrintChar(ch).start();
        //new PrintNum(number).start();

        /**
         * ReentrantLock & Condition implementation
         */
        Lock lock = new ReentrantLock();
        Condition nCondition = lock.newCondition();
        Condition cCondition = lock.newCondition();
        Thread printCharThread = new PrintCharV2(ch, nCondition, cCondition, lock);
        Thread printNumberThread = new PrintNumV2(number, nCondition, cCondition, lock);
        printCharThread.start();
        printNumberThread.start();

        /**
         * LockSupport implementation
         */
       /* Thread printCharLockSupportInstance = new PrintCharLockSupport(ch);
        Thread printNumberLockSupportInstance = new PrintNumberLockSupport(number);
        ((PrintCharLockSupport) printCharLockSupportInstance).setDepends(printNumberLockSupportInstance);
        ((PrintNumberLockSupport) printNumberLockSupportInstance).setDepends(printCharLockSupportInstance);
        printCharLockSupportInstance.start();
        printNumberLockSupportInstance.start();*/

        /**
         * blocking queue implementation
         */

        /*BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(2);
        Thread printCharWithBlockingQueue = new PrintCharWithBlockingQueue(ch, blockingQueue);
        Thread printNumWithBlockingQueue = new PrintNumWithBlockingQueue(number, blockingQueue);
        printCharWithBlockingQueue.start();
        printNumWithBlockingQueue.start();*/

    }


    private static class PrintCharWithBlockingQueue extends Thread {

        private char[] ch;
        private BlockingQueue<String> queue;

        private PrintCharWithBlockingQueue(char[] ch, BlockingQueue<String> queue) {
            this.ch = ch;
            this.queue = queue;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < ch.length; index++) {
                try {
                    if (!stop2) {
                        queue.put("--");
                        queue.put("--");
                        System.out.print(ch[index]);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            stop1 = true;
        }
    }

    private static class PrintNumWithBlockingQueue extends Thread {
        private int[] number;
        private BlockingQueue<String> queue;

        private PrintNumWithBlockingQueue(int[] number, BlockingQueue<String> queue) {
            this.number = number;
            this.queue = queue;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < number.length; index++) {
                try {
                    if (!stop1) {
                        queue.take();
                        queue.take();
                        System.out.print(number[index]);
                    } else {
                        System.out.println(number[index]);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            stop2 = true;
        }
    }


    private static class PrintCharLockSupport extends Thread {
        private char[] ch;
        private Thread depends;

        public void setDepends(Thread thread) {
            this.depends = thread;
        }

        public PrintCharLockSupport(char[] ch) {
            this.ch = ch;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < ch.length; index++) {
                System.out.print(ch[index]);
                LockSupport.unpark(depends);
                if (!stop2)
                    LockSupport.park();
            }
            LockSupport.unpark(depends);
            stop1 = true;
        }
    }

    private static class PrintNumberLockSupport extends Thread {
        private int[] number;
        private Thread depends;

        public void setDepends(Thread thread) {
            this.depends = thread;
        }

        public PrintNumberLockSupport(int[] number) {
            this.number = number;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < number.length; index++) {
                System.out.print(number[index]);
                LockSupport.unpark(depends);
                if (!stop1)
                    LockSupport.park();
            }
            LockSupport.unpark(depends);
            stop2 = true;
        }
    }

    private static class PrintChar extends Thread {
        private char[] ch;

        public PrintChar(char[] ch) {
            this.ch = ch;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < ch.length; index++) {
                synchronized (clock) {
                    clock.notify();
                    System.out.print(ch[index]);
                    try {
                        if (index != ch.length - 1) {
                            clock.wait();
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private static class PrintNum extends Thread {
        private int[] number;

        public PrintNum(int[] number) {
            this.number = number;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < number.length; index++) {
                synchronized (clock) {
                    clock.notify();
                    System.out.print(number[index]);
                    try {
                        if (index != number.length - 1)
                            clock.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private static final class PrintNumV2 extends Thread {
        private int[] num;
        private Condition nCondition, cCondition;
        private Lock lock;

        public PrintNumV2(int[] num, Condition nCondition, Condition cCondition, Lock lock) {
            this.num = num;
            this.nCondition = nCondition;
            this.cCondition = cCondition;
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < num.length; index++) {
                System.out.print(num[index]);
                lock.lock();
                try {
                    if (!stop1) {
                        cCondition.signal();
                        nCondition.await();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
            try {
                lock.lock();
                cCondition.signal();
            } finally {
                lock.unlock();
            }
            stop2 = true;
        }
    }

    private static final class PrintCharV2 extends Thread {
        private char[] ch;
        private Condition nCondition, cCondition;
        private Lock lock;

        public PrintCharV2(char[] ch, Condition nCondition, Condition cCondition, Lock lock) {
            this.ch = ch;
            this.nCondition = nCondition;
            this.cCondition = cCondition;
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            for (int index = 0; index < ch.length; index++) {
                System.out.print(ch[index]);
                lock.lock();
                try {
                    if (!stop2) {
                        nCondition.signal();
                        cCondition.await();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
            try {
                //System.out.println("ch is over, notify the num print thread");
                lock.lock();
                nCondition.signal();
            } finally {
                lock.unlock();
            }
            stop1 = true;
        }
    }


}


