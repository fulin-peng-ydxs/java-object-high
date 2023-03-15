package sync.reentrant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author engFuLin
 * @description  lock锁对象的可重入机制
 * @date 12:04 2022/4/24
 **/
public class ReentrantLockReentrant {

    static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            lock.lock();
            try{
                System.out.println("=======外层");
                lock.lock();
                try{
                    System.out.println("=======内层");
                }finally {
                    lock.unlock();
                }
            } finally {
                //正在情况，加锁几次就要解锁几次
                lock.unlock();
                lock.unlock();
            }
        },"t1").start();
        new Thread(() -> {
            lock.lock();
            try{
                System.out.println("b thread----外层调用lock");
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        },"b").start();
    }
}


