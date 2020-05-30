package com.study.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @description: Future使用
 * <p>
 * 实现callable接口，同时重写call方法,方法submit不仅可以传入Callable对象，
 * 也可以传入Runnable对象，说明submit()方法支持有返回值和无返回值的功能
 * get具有阻塞性，而isDone不阻塞
 * Callable<void>
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:28
 **/
@Slf4j
public class FutureTest2 {
    public static void main(String[] args) {
        try{
/*        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                    System.out.println("查看get结果打印信息");
                }
            };
        */
           //采用lambda表达式
            Runnable runnable = ()->System.out.println("查看get结果打印信息");
            ExecutorService executor = Executors.newCachedThreadPool();
            Future future = executor.submit(runnable);
            //此时get返回的值为null，说明支持void的方式
            System.out.println("future get result:"+future.get()+" and isDone:"+future.isDone());
        }catch(Exception e){
                log.error("do the thing error:{}"+e.getMessage());
        }
    }
}
