package com.mlamp;

import java.util.concurrent.locks.ReentrantLock;

public class InterruptedLock {

    public static void main(String[] args) throws InterruptedException {
        /*BufferInterrupt bufferInterrupt = new BufferInterrupt();
        final Writer writer = new Writer(bufferInterrupt);
        final Reader reader = new Reader(bufferInterrupt);
        writer.start();
        Thread.sleep(1000);
        reader.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (; ; ) {
                    if (System.currentTimeMillis() - start > 5000) {
                        System.out.println("no wait, try to interrupt");
                        reader.interrupt();
                        writer.interrupt();
                        break;
                    }
                }
            }
        }).start();*/
        /*Thread thread = new InterruptedThread();
        thread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (; ; ) {
                    if (System.currentTimeMillis() - start > 3000) {
                        System.out.println("no wait, try to interrupt");
                        thread.interrupt();
                        break;
                    }
                }
            }
        }).start();*/

       /* BufferSynchronizeVersion bufferSynchronizeVersion = new BufferSynchronizeVersion();
        final Thread writer = new WriteSynchronizeVersion(bufferSynchronizeVersion);
        final Thread reader = new ReaderSynchronizeVersion(bufferSynchronizeVersion);
        writer.start();
        Thread.sleep(1000);
        reader.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (; ; ) {
                    if (System.currentTimeMillis() - start > 2000) {
                        System.out.println("no wait, try to interrupt");
                        reader.interrupt();
                        break;
                    }
                }
            }
        }).start();*/


        BufferSynchronizeVersion2 bufferSynchronizeVersion = new BufferSynchronizeVersion2();
        final Object lock = new Object();
        final Thread writer = new WriteSynchronizeVersion2(bufferSynchronizeVersion, lock);
        final Thread reader = new ReaderSynchronizeVersion2(bufferSynchronizeVersion, lock);
        writer.start();
        Thread.sleep(1000);
        reader.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (; ; ) {
                    if (System.currentTimeMillis() - start > 2000) {
                        System.out.println("no wait, try to interrupt");
                        writer.interrupt();
                        break;
                    }
                }
            }
        }).start();


    }

    private static final class BufferSynchronizeVersion2 {
        private static final Object lock = new Object();

        public void write() throws InterruptedException {
            long startTime = System.currentTimeMillis();
            System.out.println("begin to write");
            for (; ; ) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("running interrupted");
                }
                if (System.currentTimeMillis() - startTime > Integer.MAX_VALUE) {
                    break;
                }
            }
            System.out.println("write done");
        }

        public void read() {
            System.out.println("read");
        }
    }

    private static final class BufferSynchronizeVersion {
        private static final Object lock = new Object();

        public void write() {
            synchronized (lock) {
                long startTime = System.currentTimeMillis();
                System.out.println("begin to write");
                for (; ; ) {
                    if (System.currentTimeMillis() - startTime > 5000) {
                        break;
                    }
                }
                System.out.println("write done");
            }
        }

        public void read() {
            synchronized (lock) {
                System.out.println("read");
            }
        }
    }


    private static class BufferInterrupt {
        private ReentrantLock lock = new ReentrantLock();

        public void write() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                long startTime = System.currentTimeMillis();
                System.out.println("begin write to buffer");
                for (; ; ) { // 模拟耗时操作
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("running interrupted");
                    }
                    if (System.currentTimeMillis() - startTime > Integer.MAX_VALUE) {
                        break;
                    }
                    //Thread.sleep(5000);
                }
                System.out.println(" write done");
            } finally {
                lock.unlock();
            }
        }

        public void read() throws InterruptedException {
            lock.lockInterruptibly();///可以响应中断，抛出中断异常
            try {
                System.out.println("read from buffer");
            } finally {
                lock.unlock();
            }
        }
    }

    private static final class WriteSynchronizeVersion extends Thread {
        private BufferSynchronizeVersion bufferSynchronizeVersion;

        public WriteSynchronizeVersion(BufferSynchronizeVersion bufferSynchronizeVersion) {
            this.bufferSynchronizeVersion = bufferSynchronizeVersion;
        }

        @Override
        public void run() {
            super.run();
            bufferSynchronizeVersion.write();
        }
    }

    private static final class WriteSynchronizeVersion2 extends Thread {
        private BufferSynchronizeVersion2 bufferSynchronizeVersion;
        private Object lock;

        public WriteSynchronizeVersion2(BufferSynchronizeVersion2 bufferSynchronizeVersion, Object lock) {
            this.bufferSynchronizeVersion = bufferSynchronizeVersion;
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                try {
                    bufferSynchronizeVersion.write();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }


    private static final class Writer extends Thread {
        private BufferInterrupt bufferInterrupt;

        public Writer(BufferInterrupt bufferInterrupt) {
            this.bufferInterrupt = bufferInterrupt;
        }

        @Override
        public void run() {
            super.run();
            try {
                bufferInterrupt.write();
                /**
                 *  线程在wait或sleep期间被中断了
                 */
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.out.println("write interrupted");
            }
        }
    }

    private static final class ReaderSynchronizeVersion extends Thread {
        private BufferSynchronizeVersion bufferSynchronizeVersion;

        public ReaderSynchronizeVersion(BufferSynchronizeVersion bufferSynchronizeVersion) {
            this.bufferSynchronizeVersion = bufferSynchronizeVersion;
        }

        @Override
        public void run() {
            super.run();
            bufferSynchronizeVersion.read();
        }
    }

    private static final class ReaderSynchronizeVersion2 extends Thread {
        private BufferSynchronizeVersion2 bufferSynchronizeVersion;
        private Object lock;


        public ReaderSynchronizeVersion2(BufferSynchronizeVersion2 bufferSynchronizeVersion, Object lock) {
            this.bufferSynchronizeVersion = bufferSynchronizeVersion;
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            synchronized (lock) {// it's disabled
                bufferSynchronizeVersion.read();
            }
        }
    }

    private static final class Reader extends Thread {
        private BufferInterrupt bufferInterrupt;

        public Reader(BufferInterrupt bufferInterrupt) {
            this.bufferInterrupt = bufferInterrupt;
        }

        @Override
        public void run() {
            super.run();
            try {
                bufferInterrupt.read();
            } catch (InterruptedException ex) {
                System.out.println(" read interrupted");
            }
            System.out.println("read thread does something other");
        }
    }

    private static final class InterruptedThread extends Thread {
        public static volatile boolean flag = true;

        @Override
        public void run() {
            super.run();

            /**
             * this.interrupted():测试当前线程是否已经中断（静态方法）。如果连续调用该方法，则第二次调用将返回false。
             * 在api文档中说明interrupted()方法具有清除状态的功能。执行后具有将状态标识清除为false的功能。
             *
             * this.isInterrupted():测试线程是否已经中断，但是不能清除状态标识。
             *
             *
             *
             * */
            /*
             * 不管循环里是否调用过线程阻塞的方法如sleep、join、wait，这里还是需要加上
             * !Thread.currentThread().isInterrupted()条件，虽然抛出异常后退出了循环，显
             * 得用阻塞的情况下是多余的，但如果调用了阻塞方法但没有阻塞时，这样会更安全、更及时。
             */
            while (flag && (!Thread.currentThread().isInterrupted())) {
                try {
                    //当sleep方法抛出InterruptedException  中断状态也会被清掉
                    Thread.sleep(1000);
                    System.out.println("running");
                } catch (InterruptedException ex) {

                    /**
                     * 对于处于sleep，join等操作的线程，如果被调用interrupt()后，会抛出InterruptedException，
                     * 然后线程的中断标志位会由true重置为false，因为线程为了处理异常已经重新处于就绪状态。
                     */
                    ex.printStackTrace();
                    System.out.println(Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                }
            }

        }
    }


}
