package com.study.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 *
 * @description: Future使用
 * <p>
 * 实现callable接口，同时重写call方法，其优点：与Runnable不同的是，其可以返回结果，
 * 同时可以声明异常，返回一个执行检查的异常信息，而Runnable返回的是void,
 * 因此在程序上方便排查问题，同时了解执行的结果情况,如果返回的结果想是void的，则可以在实现时选择
 * Callable<void>
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:23
 **/
@Slf4j
public class FutureTest {
     static class MyCallable implements Callable<String>{
        public String call() throws Exception {
            //业务逻辑执行部分
            log.info("do something in callable");
            Thread.sleep(1000);
            return "Done";
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //ExecutorService executorService = Executors.newCachedThreadPool();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2,3,5L,TimeUnit.SECONDS,new LinkedBlockingDeque<>());
        //执行需要提交的任务
        Future<String> future = executorService.submit(new MyCallable());
        log.info("do something in main");
        Thread.sleep(1000);
        //拿到执行后返回的结果
        String result = future.get();
        log.info("result:{}",result);
        executorService.shutdown();
    }

}
