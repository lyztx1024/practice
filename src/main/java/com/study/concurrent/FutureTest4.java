package com.study.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @description: Future使用
 * <p>
 * 实现callable接口，同时重写call方法,方法submit不仅可以传入Callable对象，
 * 而且还可以Runnable，同时可以从api中可以看到submit(Runnable,result)
 * get具有阻塞性，而isDone不阻塞。cancel与isCancelled使用
 * 其优点是从线程中返回数据以便进行后期的处理，其缺点是具有阻塞性
 * Callable<void>
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:48
 **/
@Slf4j
public class FutureTest4 {

    public static void main(String[] args) {
        try {
            MyCallable callable1 = new MyCallable("123", 5000);
            MyCallable callable2 = new MyCallable("456", 4000);
            MyCallable callable3 = new MyCallable("236", 3000);
            MyCallable callable4 = new MyCallable("678", 2000);
            MyCallable callable5 = new MyCallable("789", 1000);

            List<Callable> callableList = new ArrayList<>();
            callableList.add(callable1);
            callableList.add(callable2);
            callableList.add(callable3);
            callableList.add(callable4);
            callableList.add(callable5);

            List<Future> futureList = new ArrayList<>();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
            for (int i = 0; i < 5; i++) {
                futureList.add(executor.submit(callableList.get(i)));
            }
            System.out.println("run first time=" + System.currentTimeMillis());
            for (int i = 0; i < 5; i++) {
                System.out.println(futureList.get(i).get()+" "+System.currentTimeMillis());
            }
        } catch (Exception e) {
           log.error("执行任务出错:{}",e.getMessage());
        }
    }
}
