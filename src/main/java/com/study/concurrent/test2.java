package com.study.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class test2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testFuture();
//        testCompletionService();
    }

    //结果的输出和线程的放入顺序 有关(如果前面的没完成，就算后面的哪个完成了也得等到你的牌号才能输出！)，so阻塞耗时
    public static void testFuture() throws InterruptedException, ExecutionException {
        System.out.println("--1.1--> main Thread begin:");
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> result = new ArrayList<Future<String>>();
        for (int i = 0; i < 10; i++) {
            Future<String> submit = executor.submit(new Task(i));
            result.add(submit);
        }
        executor.shutdown();
        for (int i = 0; i < 10; i++) {//一个一个等待返回结果
            System.out.println("--1.2--> 一个一个等待返回结果: " + result.get(i).get());
        }
        System.out.println("--1.3--> main Thread end:");
    }

    //结果的输出和线程的放入顺序 无关(谁完成了谁就先输出！主线程总是能够拿到最先完成的任务的返回值，而不管它们加入线程池的顺序)，so很大大缩短等待时间
    private static void testCompletionService() throws InterruptedException, ExecutionException {
        System.out.println("--2.2--> main Thread begin:");
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < 10; i++) {
            completionService.submit(new Task(i));
        }
        executor.shutdown();
        for (int i = 0; i < 10; i++) {
            // 检索并移除表示下一个已完成任务的 Future，如果目前不存在这样的任务，则等待。
            Future<String> future = completionService.take(); //这一行没有完成的任务就阻塞
            System.out.println("--2.3--> " + future.get());   // 这一行在这里不会阻塞，引入放入队列中的都是已经完成的任务
        }
        System.out.println("--2.4--> main Thread end:");
    }

    private static class Task implements Callable<String> {

        private volatile int i;

        public Task(int i) {
            this.i = i;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName());
            return "--0.1--> 任务 : " + i;
        }

    }
}