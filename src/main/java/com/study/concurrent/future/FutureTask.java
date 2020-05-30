package com.study.concurrent.future;

import com.study.concurrent.util.ThreadPoolBuilder;
import com.study.concurrent.util.ThreadPoolUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @description: Future使用
 * <p>
 * FutureTask:是Future的实现类，而且在使用线程池时，默认的情况下也是使用
 * futureTask类作为接口Future的实现类，但需要注意的是，Future接口调用get()方法
 * 取得处理的结果时是阻塞性的，也就是如果调用get()方法时，任务尚未完成，则
 * 调用get()方法时一直阻塞到此任务完成时为止。如果是这样的相关，则前面先执行的任务
 * 一旦耗时很多，则后面的任务调用get()方法就呈现阻塞状态，也就是排队等待，大大影响运行效率。
 * 也即主线程并不能保证首先获的是最先完成任务的返回值，这是future的缺点，影响效率
 * </p>
 * @author: lyz
 * @date: 2020/05/24 12:00
 **/
public class FutureTask {
    /**
     * FutureTask由线程池执行
     */
    private static void exeForPool(){
        // 创建 FutureTask,采用三个线程执行主线程
        java.util.concurrent.FutureTask<Integer> futureTask = new java.util.concurrent.FutureTask<>(()-> 1+2);
        // 创建线程池
        ThreadPoolExecutor executor = ThreadPoolBuilder.fixedPool().build();

        try{
            // 提交 FutureTask
            executor.submit(futureTask);
            // 获取计算结果
            Integer result = futureTask.get();
            System.out.println(result);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            //进行优雅关闭
            ThreadPoolUtil.gracefulShutdown(executor,1);

        }
    }

    /**
     * FutureTask由线程处理
     */
    private static void exeForThread(){
        // 创建 FutureTask
        java.util.concurrent.FutureTask<Integer> futureTask = new java.util.concurrent.FutureTask<>(()-> 1+2);
        // 创建并启动线程
        Thread T1 = new Thread(futureTask);
        T1.start();
        // 获取计算结果
        try{
            Integer result = futureTask.get();
            System.out.println(result);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 利用FutureTask实现烧水泡茶
     */
    private static void fireWater(){

        // 创建任务 T2 的 FutureTask
        java.util.concurrent.FutureTask<String> ft2 = new java.util.concurrent.FutureTask<>(new T2Task());
        // 创建任务 T1 的 FutureTask
        java.util.concurrent.FutureTask<String> ft1 = new java.util.concurrent.FutureTask<>(new T1Task(ft2));
        // 线程 T1 执行任务 ft1
        Thread t1 = new Thread(ft1);
        t1.start();
        // 线程 T2 执行任务 ft2
        Thread t2 = new Thread(ft2);
        t2.start();
        // 等待线程 T1 执行结果
        try{
            System.out.println(ft1.get());

        }catch (Exception e){
            e.printStackTrace();
        }



    }
    /**
     * 洗水壶、烧开水、泡茶，实现Callable接口，重写call方法
     */
    static class T1Task implements Callable<String> {
        java.util.concurrent.FutureTask<String> ft2;
        T1Task(java.util.concurrent.FutureTask<String> ft2){
            this.ft2 = ft2;
        }
        @Override
        public String call() throws Exception {
            System.out.println("T1: 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T1: 烧开水...");
            TimeUnit.SECONDS.sleep(15);
            // 获取 T2 线程的茶叶
            String tf = ft2.get();
            System.out.println("T1: 拿到茶叶:"+tf);

            System.out.println("T1: 泡茶...");
            return " 上茶:" + tf;
        }
    }
    /**
     * 洗茶壶、洗茶杯、拿茶叶，实现Callable接口，重写call方法
     */
    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T2: 洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T2: 洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("T2: 拿茶叶...");
            TimeUnit.SECONDS.sleep(1);
            return " 白茶 ";
        }
    }
    public static void main(String[] args) {
        exeForPool();
        exeForThread();
        fireWater();
    }
}

