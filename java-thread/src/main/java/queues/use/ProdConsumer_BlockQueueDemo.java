package queues.use;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: hhf
 * @create: 2020-05-07 17:03
 *
 * valatile/cas/atomicInteger/BlockQueue/线程交互/原子引用整合的生产者消费者案例
 **/

class MyResource
{
    private volatile boolean FLAG = true; //默认开启，进行生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    //生产者
    public void MyProd() throws Exception{
        String data = null;
        boolean retValue ; //默认是false

        while (FLAG)
        {
//往阻塞队列填充数据
            data = atomicInteger.incrementAndGet()+"";//等于++i的意思
            retValue = blockingQueue.offer(data,2L, TimeUnit.SECONDS);
            if (retValue){ //如果是true，那么代表当前这个线程插入数据成功
                System.out.println(Thread.currentThread().getName()+"\t插入队列"+data+"成功");
            }else {  //那么就是插入失败
                System.out.println(Thread.currentThread().getName()+"\t插入队列"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
//如果FLAG是false了，马上打印
        System.out.println(Thread.currentThread().getName()+"\t大老板叫停了，表示FLAG=false,生产结束");
    }

    //消费者
    public void MyConsumer() throws Exception
    {
        String result = null;
        while (FLAG) { //开始消费
//两秒钟等不到生产者生产出来的数据就不取了
            result = blockingQueue.poll(2L,TimeUnit.SECONDS);
            if (null == result || result.equalsIgnoreCase("")){ //如果取不到数据了
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+"\t 超过两秒钟没有取到数据，消费退出");
                System.out.println();
                System.out.println();
                return;//退出
            }
            System.out.println(Thread.currentThread().getName()+"\t消费队列数据"+result+"成功");
        }
    }

    //叫停方法
    public void stop() throws Exception{
        this.FLAG = false;
    }

}

public class ProdConsumer_BlockQueueDemo {
    public static void main(String[] args)  throws Exception{
        MyResource myResource = new MyResource(new ArrayBlockingQueue(10));
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 生产线程启动");
            try {
                myResource.MyProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Prod").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName()+"\t 消费线程启动");
            System.out.println();
            System.out.println();
            try {
                myResource.MyConsumer();
                System.out.println();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"Consumer").start();

        try { TimeUnit.SECONDS.sleep(5); }catch (Exception e) {e.printStackTrace();}
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("5秒钟时间到，大bossMain主线程叫停，活动结束");
        myResource.stop();
    }
}


