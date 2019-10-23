package time.wio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lxt-PC on 2017/12/17.
 */
public class TimeServerHandlerExecutePool{
    private ExecutorService executorService;

    /**
     *
     * @param maxPoolSize 线程池内线程最大数量
     * @param queueSize   队列深度
     */
    public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize) {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()
        ,maxPoolSize,120L, TimeUnit.SECONDS
        ,new ArrayBlockingQueue<Runnable>(queueSize));


    }
    public  void execute(Runnable task){
        executorService.execute(task);
    }
}
