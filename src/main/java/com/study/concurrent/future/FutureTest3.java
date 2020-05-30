package com.study.concurrent.future;

import com.study.concurrent.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @description: Future使用
 * <p>
 * 实现callable接口，同时重写call方法,方法submit不仅可以传入Callable对象，
 * 而且还可以Runnable，同时可以从api中可以看到submit(Runnable,result)携带返回信息
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:38
 **/
@Slf4j
public class FutureTest3 {

    //实现Runnable接口,重写run方法
    static class MyRunnable implements Runnable{
        private User user;
        public MyRunnable(User user){
            this.user = user;
        }

        @Override
        public void run() {
          user.setUsername("在路上");
          user.setPassword("123456");
        }
    }

    public static void main(String[] args) {
       try{
           User user =new User("123","345");
           MyRunnable myRunnable = new MyRunnable(user);
           ThreadPoolExecutor executor = new ThreadPoolExecutor(10,10,10, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
           Future<User> future = executor.submit(myRunnable,user);
           System.out.println("start Time="+System.currentTimeMillis());
           user = future.get();
           System.out.println("get Value="+user.getUsername()+"==="+user.getPassword());
           System.out.println("end Time="+System.currentTimeMillis());
       }catch (Exception e){
           log.info("执行异常:{}",e.getMessage());
       }
    }


}
