package com.study.concurrent.callable;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 *
 * @description: Future使用
 * <p>
 * 实现callable接口，同时重写call方法,方法submit不仅可以传入Callable对象，
 * 而且还可以Runnable，同时可以从api中可以看到submit(Callable<T> task)
 * get具有阻塞性，而isDone不阻塞
 * Callable<void>
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:48
 **/
@Slf4j
public class CallableTask {

   static class Task implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("启动线程进行计算操作");
            int sum = 0;
            for (int i= 0;i<1000;i++){
                sum += i;
            }
            TimeUnit.SECONDS.sleep(3);
            return new Integer(sum);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,3,5L,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
        Task task = new Task();
        Future<Integer> result = executor.submit(task);
        executor.shutdown();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("主线程在执行任务");
        try{
            System.out.println("计算结果为:"+result.get());
        }catch(Exception e){
            log.info("任务执行异常:{}",e.getMessage());
        }
        System.out.println("所有任务执行完毕");
    }

}
