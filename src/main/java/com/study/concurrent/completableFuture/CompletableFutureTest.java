package com.study.concurrent.completableFuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

/**
 *
 * @description: CompletableFuture使用
 * <p>
 *  此demo的素材来源于https://www.jianshu.com/p/6bac52527ca4
 *  如果需要学习，请参考原文
 *  我将其加工成lambda表达式进行展示
 * </p>
 * @author: lyz
 * @date: 2020/05/24 11:48
 **/
public class CompletableFutureTest {
    public static void main(String[] args) throws Exception {
        //runAsync();
        //supplyAsync();
       // whenComplete();
       // theApply();
       // handle();
       // thenAccept();
       // thenCombine();
       // thenAcceptBoth();
      //  applyToEither();
       // acceptEither();
       // runAfterEither();
        //runAfterBoth()
        thenCompose();

    }

    /**=====1、 runAsync 和 supplyAsync方法 ========**/
    /**
     * public static CompletableFuture<Void> runAsync(Runnable runnable)
     * public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
     * public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
     */
    //无返回值
    public  static void runAsync() throws Exception{
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            try{
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
                System.out.println("运行结束。。。");
            }
        });
        future.get();
    }

    public static void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(()->{
            try{
               TimeUnit.SECONDS.sleep(1);
            }catch (Exception e){

            }
            System.out.println("运行结束,你可以看到运行时间。。。");
            return System.currentTimeMillis();
        });
        long time = future.get();
        System.out.println("time="+time);
    }

    /**===============2.计算结果完成时的回调方法========**/
    /**
     * public CompletableFuture<T> whenComplete(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action)
     * public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T,? super Throwable> action, Executor executor)
     * public CompletableFuture<T> exceptionally(Function<Throwable,? extends T> fn)
     */
    //当CompletableFuture的计算结果完成，或者抛出异常，可以执行特定的Action
    //whenComplete 和 whenCompleteAsync 的区别：
    //whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
    //whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
    public static void whenComplete() throws InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
           try{
               TimeUnit.SECONDS.sleep(2);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           if(new Random().nextInt()%2>=0){
               int i =12/0;
           }
            System.out.println("运行结束。。。");
        });

       /* future.whenComplete(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void aVoid, Throwable throwable) {
                System.out.println("执行完成");
            }
        });*/
        future.whenComplete((Void aVoid, Throwable throwable)->{
                System.out.println("执行完成");
        });
        /*future.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                System.out.println("执行失败！"+throwable.getMessage());
                return null;
            }
        });*/
        future.exceptionally((Throwable throwable)->{
                System.out.println("执行失败！"+throwable.getMessage());
                return null;
        });
        TimeUnit.SECONDS.sleep(2);
    }

    //3.theAppliy方法：当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化
    /**
     * public <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
     * public <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     */
    public static void theApply() throws ExecutionException, InterruptedException {
        /*CompletableFuture<Long> future = CompletableFuture.supplyAsync(new Supplier<Long>(){
            @Override
            public Long get() {
                //执行任务一的业务
                long  result = new Random().nextInt(100);
                System.out.println("result="+result);
                return result;
            }

        }).thenApply(new Function<Long, Long>() {
            @Override
            public Long apply(Long aLong) {
                //任务二的业务逻辑
                long result = aLong*5;
                return result;
            }
        });*/
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(()->{
                //执行任务一的业务
                long  result = new Random().nextInt(100);
                System.out.println("result="+result);
                return result;

        }).thenApply((Long aLong)->{
                //任务二的业务逻辑
                long result = aLong*5;
                return result;
        });
        long result = future.get();
        System.out.println(result);
    }


    /**======4.handle方法: handle是执行任务完成时对结果的处理========**/
    //handle方法和thenApply方法处理方式基本一样，不同的是handle是在任务完成后再执行
    //还可以处理异常的任务。theApply只可以执行正常任务，任务出现异常则不会执行theApply方法
    /**
     * public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);
     * public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn);
     * public <U> CompletionStage<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn,Executor executor);
     */
    public  static void handle() throws ExecutionException, InterruptedException {
       /* CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int i= 10/0;
                return new Random().nextInt(10);
            }
        }).handle(new BiFunction<Integer, Throwable, Integer>() {
            @Override
            public Integer apply(Integer param, Throwable throwable) {
                int result = -1;
                if(throwable==null){
                    result = param * 2;
                }else{
                    System.out.println(throwable.getMessage());
                }
                return result;
            }
        });*/
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
                int i= 10/0;
                return new Random().nextInt(10);
        }).handle((Integer param, Throwable throwable)->{
                int result = -1;
                if(throwable==null){
                    result = param * 2;
                }else{
                    System.out.println(throwable.getMessage());
                }
                return result;
        });
        System.out.println(future.get());
    }

    /**===========5.thenAccept消费处理结果:接收任务的处理结果，并消费处理，无返回结果==========**/
    /**
     * public CompletionStage<Void> thenAccept(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
     * public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
     */
    //该方法只是消费执行完成的任务，并可以根据上面的任务返回的结果进行处理。并没有后续的输错操作
    public static void thenAccept() throws Exception{
       /* CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(10);
            }
        }).thenAccept(integer -> {
            System.out.println(integer);
        });*/
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->
                new Random().nextInt(10)).thenAccept(System.out::println);
        future.get();
    }

   /**=========6.run方法:跟thenAccept方法不一样的是，不关心任务的处理结果。只要上面的任务执行完成，就开始执行 thenAccept====**/
    /**
     * public CompletionStage<Void> thenRun(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action);
     * public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
     */
    //不同的是上个任务处理完成后，并不会把计算的结果传给 thenRun 方法。只是处理玩任务后，执行 thenAccept 的后续操作
    public static void thenRun() throws Exception{
       /* CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return new Random().nextInt(10);
            }
        }).thenRun(() -> {
            System.out.println("thenRun ...");
        });*/
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->
                new Random().nextInt(10)).thenRun(() -> System.out.println("thenRun ..."));
        future.get();
    }

   /**====7.thenCombine 合并任务:thetionCombine 会把 两个 ComplenStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理=====**/
    /**
     * public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
     * public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
     */
    //这个就有点forkJoin的味道了
    private static void thenCombine() throws Exception {
  /*      CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        });
        CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String t, String u) {
                return t+" "+u;
            }
        });*/
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()-> "hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> "hello");
        CompletableFuture<String> result = future1.thenCombine(future2, (t, u)-> t+" "+u);
        System.out.println(result.get());
    }

    /**====8.thenAcceptBoth:当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗=======**/
    /**
     * public <U> CompletionStage<Void> thenAcceptBoth(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action);
     * public <U> CompletionStage<Void> thenAcceptBothAsync(CompletionStage<? extends U> other,BiConsumer<? super T, ? super U> action,     Executor executor);
     */
    //
    private static void thenAcceptBoth() throws Exception {
       /* CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.thenAcceptBoth(f2, new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer t, Integer u) {
                System.out.println("f1="+t+";f2="+u+";");
            }
        });*/
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
        });
        f1.thenAcceptBoth(f2, (t, u)->System.out.println("f1="+t+";f2="+u+";"));
    }

    /**====== 9.applyToEither 方法:两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的转化操作========**/
    /**
     * public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn);
     * public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn,Executor executor);
     *
     */
    private static void applyToEither() throws Exception {
       /* CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });

        CompletableFuture<Integer> result = f1.applyToEither(f2, new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer t) {
                System.out.println(t);
                return t * 2;
            }
        });
*/
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(4);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;

        });
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(4);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
        });

        CompletableFuture<Integer> result = f1.applyToEither(f2, (Integer t)->{
                System.out.println(t);
                return t * 2;
        });
        System.out.println(result.get());
    }

   /**====10.acceptEither 方法:两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的消耗操作======**/
    /**
     * public CompletionStage<Void> acceptEither(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action);
     * public CompletionStage<Void> acceptEitherAsync(CompletionStage<? extends T> other,Consumer<? super T> action,Executor executor);
     */
    private static void acceptEither() throws Exception {
      /*  CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.acceptEither(f2, new Consumer<Integer>() {
            @Override
            public void accept(Integer t) {
                System.out.println(t);
            }
        });
*/
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(5);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(()-> {
                int t = new Random().nextInt(5);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
        });
       // f1.acceptEither(f2, t-> System.out.println(t));
        f1.acceptEither(f2, System.out::println);
    }

    /**===11.runAfterEither 方法:两个CompletionStage，任何一个完成了都会执行下一步的操作（Runnable）===**/
    /**
     * public CompletionStage<Void> runAfterEither(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterEitherAsync(CompletionStage<?> other,Runnable action,Executor executor);
     */
    private static void runAfterEither() throws Exception {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.runAfterEither(f2, new Runnable() {

            @Override
            public void run() {
                System.out.println("上面有一个已经完成了。");
            }
        });
    }

   /**====12.runAfterBoth:两个CompletionStage，都完成了计算才会执行下一步的操作（Runnable）  =======**/
    /**
     * public CompletionStage<Void> runAfterBoth(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action);
     * public CompletionStage<Void> runAfterBothAsync(CompletionStage<?> other,Runnable action,Executor executor);
     */
    private static void runAfterBoth() throws Exception {
      /*  CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(6);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
            }
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(6);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
            }
        });
        f1.runAfterBoth(f2, new Runnable() {

            @Override
            public void run() {
                System.out.println("上面两个任务都执行完成了。");
            }
        });*/
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(6);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f1="+t);
                return t;
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(()->{
                int t = new Random().nextInt(6);
                try {
                    TimeUnit.SECONDS.sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("f2="+t);
                return t;
        });
        f1.runAfterBoth(f2, ()-> System.out.println("上面两个任务都执行完成了。"));
    }

    /**=====13.thenCompose 方法:thenCompose 方法允许你对两个 CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作=======**/
    /**
     * public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn);
     * public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) ;
     * public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) ;
     */
    private static void thenCompose() throws Exception {
        /*CompletableFuture<Integer> f = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                int t = new Random().nextInt(3);
                System.out.println("t1="+t);
                return t;
            }
        }).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer param) {
                return CompletableFuture.supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        int t = param *2;
                        System.out.println("t2="+t);
                        return t;
                    }
                });
            }

        });*/
        CompletableFuture<Integer> f = CompletableFuture.supplyAsync(()->{
            int t = new Random().nextInt(7);
            System.out.println("t1="+t);
            return t;
        }).thenCompose((Integer param)->
                CompletableFuture.supplyAsync(()->{
                    int t = param *2;
                    System.out.println("t2="+t);
                    return t;
                }));
        System.out.println("thenCompose result : "+f.get());
    }

}
