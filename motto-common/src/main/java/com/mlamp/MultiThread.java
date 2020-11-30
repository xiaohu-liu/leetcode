package com.mlamp;

import sun.awt.windows.ThemeReader;

import java.util.concurrent.*;

public class MultiThread {

    private static void shutdownThreadPoolGracefully(ExecutorService executorService) {
        if (executorService == null || executorService.isTerminated()) return;
        executorService.shutdown(); // Disable new tasks from being submitted
        //设置最大尝试次数
        try {
            // wait for 60 seconds
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                //调用shutdownNow 取消正在执行的任务
                executorService.shutdownNow();
                // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println(" thread-pool ends un-normally");
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            //shutdownNow again
            executorService.shutdownNow();
        }
    }

    public static void main(String[] args) {
      /*  CountdownLatchTest countdownLatchTest = new CountdownLatchTest();
        countdownLatchTest.run();*/

       /* CountdownLatchV2 countdownLatchV2 = new CountdownLatchV2();
        countdownLatchV2.run();*/
        /*CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("都已经到达栅栏");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        CyclicBarrierSample sample1 = new CyclicBarrierSample(cyclicBarrier, "椅子面");
        new Thread(sample1).start();
        CyclicBarrierSample sample2 = new CyclicBarrierSample(cyclicBarrier, "椅子腿儿");
        new Thread(sample2).start();
        CyclicBarrierSample sample3 = new CyclicBarrierSample(cyclicBarrier, "椅子靠背");
        new Thread(sample3).start();*/
        SemaphoreSample semaphoreSample = new SemaphoreSample();
        semaphoreSample.run();
    }

    private static final class SemaphoreSample {
        public void run() {
            // the same time , only permit 10 threads to run
            Semaphore semaphore = new Semaphore(10);
            ExecutorService executors = Executors.newCachedThreadPool();

            for (int index = 0; index < 20; index++) {
                final int NO = index;
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            semaphore.acquire();
                            System.out.println(NO + " acquires");
                            TimeUnit.SECONDS.sleep((long) (Math.random() * 3));
                            semaphore.release();
                            System.out.println(NO + " releases");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                executors.execute(runnable);
            }
            shutdownThreadPoolGracefully(executors);

        }
    }


    private static final class CyclicBarrierSample implements Runnable {
        /**
         * 栅栏类似于闭锁，它能阻塞一组线程直到某个事件发生。 栅栏与闭锁的关键区别在于，
         * 所有的线程必须同时到达栅栏位置，才能继续执行。闭锁用于等待事件，而栅栏用于等待其他线程。
         */
        private CyclicBarrier cyclicBarrier;
        private String event;

        public CyclicBarrierSample(CyclicBarrier cyclicBarrier, String event) {
            this.cyclicBarrier = cyclicBarrier;
            this.event = event;
        }

        @Override
        public void run() {
            try {
                System.out.println("开始做 " + event);
                Thread.sleep((long) (Math.random() * 3000));
                cyclicBarrier.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("【" + event + "】做好了， 我们来一起组装吧！");
        }
    }


    private static final class CountdownLatchV2 {
        public void run() {
            ExecutorService executorService = Executors.newCachedThreadPool();
            final CountDownLatch gun = new CountDownLatch(1);
            final CountDownLatch run = new CountDownLatch(4);
            for (int index = 0; index < 4; index++) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(" runner " + Thread.currentThread().getName() + " waits for the gun call");
                            gun.await();
                            System.out.println("选手" + Thread.currentThread().getName() + "已接受裁判口令");
                            Thread.sleep((long) (Math.random() * 10000));
                            System.out.println("选手" + Thread.currentThread().getName() + "到达终点");
                            run.countDown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                executorService.execute(runnable);
            }

            try {
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println("裁判" + Thread.currentThread().getName() + "即将发布口令");
                gun.countDown();
                System.out.println("裁判" + Thread.currentThread().getName() + "已发送口令，正在等待所有选手到达终点");
                run.await();
                System.out.println("所有选手都到达终点");
                System.out.println("裁判" + Thread.currentThread().getName() + "汇总成绩排名");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executorService.shutdown(); // Disable new tasks from being submitted
            //设置最大尝试次数
            try {
                // wait for 60 seconds
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    //调用shutdownNow 取消正在执行的任务
                    executorService.shutdownNow();
                    // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println(" thread-pool ends un-normally");
                    }
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                //shutdownNow again
                executorService.shutdownNow();
            }

        }
    }


    private static final class CountdownLatchTest {
        public void run() {
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            final CountDownLatch countdownLatch = new CountDownLatch(3);
            for (int index = 0; index < 3; index++) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("sub thread " + Thread.currentThread().getName() + " starts ");
                            Thread.sleep((long) (Math.random() * 10000));
                            System.out.println("sub thread " + Thread.currentThread().getName() + " ends ");
                            countdownLatch.countDown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                };
                executorService.execute(runnable);
            }
            try {
                System.out.println(" main thread " + Thread.currentThread().getName() + " wait the sub threads finish ");
                countdownLatch.await();
                System.out.println(" main thread " + Thread.currentThread().getName() + " ends");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }

            executorService.shutdown(); // Disable new tasks from being submitted
            //设置最大尝试次数
            try {
                // wait for 60 seconds
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    //调用shutdownNow 取消正在执行的任务
                    executorService.shutdownNow();
                    // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                    if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                        System.err.println(" thread-pool ends un-normally");
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                //shutdownNow again
                executorService.shutdownNow();
            }

        }
    }
}
