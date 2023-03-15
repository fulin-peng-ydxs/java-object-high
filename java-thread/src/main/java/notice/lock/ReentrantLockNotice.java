package notice.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author PengFuLin
 * @description: 使用ReentrantLock完成线程通信
 * @date 2022/4/20 23:55
 */
public class ReentrantLockNotice {

    private int number=0;

    private Lock lock=new ReentrantLock();

    private Condition condition=lock.newCondition();

    //+1
    public void incr() throws InterruptedException {
        //加锁
        lock.lock();
        try {
            //防止因为多个线程导致的虚假唤醒
            while (number!=0){
                //等待被唤醒
                condition.await();
            }
            //操作
            number++;
            System.out.println(Thread.currentThread().getName()+":"+number);
            //唤醒其他线程
            condition.signalAll();
        } finally {
            //放锁
            lock.unlock();
        }
    }
    //-1
    public void decr() throws InterruptedException {
        //加锁
        lock.lock();
        try {
            //防止因为多个线程导致的虚假唤醒
            while (number==0){
                //等待被唤醒
                condition.await();
            }
            //操作
            number--;
            System.out.println(Thread.currentThread().getName()+":"+number);
            //唤醒其他线程
            condition.signalAll();
        } finally {
            //放锁
            lock.unlock();
        }
    }
}


class ThreadStart{

    public static void main(String[] args) {
        ReentrantLockNotice lockNotice = new ReentrantLockNotice();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    lockNotice.decr();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    lockNotice.decr();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    lockNotice.incr();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"CC").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    lockNotice.decr();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"DD").start();
    }
}



