package com.study.concurrent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;


 /*** @description: CompletionSerivce使用
 * <p>
 * 采用异步的方式一边处理新的任务，一边处理完成任务的结果
 * 也就是说在处理多个任务时，可以实现先处理的任务，先拿到结果
 * 采用 submit+take,不至于在一个任务没有完成的情况下，其余的结果不能处理
 * 你可以将其理解成Executor+BlockingQueue的结合体，此时你可以使用其实现
 * ExecutorCompletionService，进行异构并行
 * </p>
 * @author: lyz
 * @date: 2020/05/24 22:02
 **/
public class CompletionServiceTest {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        //开启5个线程
        ExecutorService exs = Executors.newFixedThreadPool(4);
        try {
            int taskCount = 10;
            // 结果集
            List<Integer> list = new ArrayList<Integer>();
            List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();

            // 1.定义CompletionService
            CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(exs);

            // 2.添加任务,需要执行的业务
            for (int i = 0; i < taskCount; i++) {
                Future<Integer> future = completionService.submit(new Task(i + 1));
                futureList.add(future);
            }

            // 3.获取结果
            for (int i = 0; i < taskCount; i++) {
                Integer result = completionService.take().get();
                System.out.println("任务i==" + result + "完成!" + new Date());
                list.add(result);
            }

            System.out.println("list=" + list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭线程池
            exs.shutdown();
        }

    }

    //实现Callable接口，重写call方法
    static class Task implements Callable<Integer> {
        Integer i;

        public Task(Integer i) {
            super();
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            if (i == 4) {
                Thread.sleep(5000);
            } else {
                Thread.sleep(1000);
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "任务i=" + i + ",执行完成！");
            return i;
        }

    }
}
