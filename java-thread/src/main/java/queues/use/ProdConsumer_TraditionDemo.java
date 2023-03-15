package queues.use;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



class ShareData
{
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    //加
    public void increment()throws  Exception
    {
        lock.lock();
        try{
//1.判断
            while (number != 0){
//等待，不能生产
                condition.await();
            }
//2.干活
            number++;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
//3.通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    //减
    public void decrement()throws  Exception
    {
        lock.lock();
        try{
//1.判断
            while (number == 0){
//等待，不能生产
                condition.await();
            }
//2.干活
            number--;
            System.out.println(Thread.currentThread().getName()+"\t"+number);
//3.通知唤醒
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

/**
 *
 * 传统的消费者和生产者Demo
 * 题目：一个初始值为零的变量，两个线程对其交替操作，一个加一个减一，来五轮
 */
public class  ProdConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 1; i<5 ; i++) {
                try {
                    shareData.increment();//增加
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"AAA").start();

        new Thread(() -> {
            for (int i = 1; i<5 ; i++) {
                try {
                    shareData.decrement();//减
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"BBB").start();

    }
}


