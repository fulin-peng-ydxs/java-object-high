package notice.support;

import java.util.concurrent.locks.LockSupport;


/**
 * @author PengFuLin
 * @description LockSupport类支持的线程通信机制
 * @date 2022/4/24 12:06
 */
public class LockSupportDemo1 {

    /*
     LockSupport它的解决的痛点
     1。LockSupport不用持有锁块，不用加锁，程序性能好，
     2。没有先后顺序，不容易导致卡死
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ----begi"+System.currentTimeMillis());
            LockSupport.park();//阻塞当前线程
            System.out.println(Thread.currentThread().getName() + "\t ----被唤醒"+System.currentTimeMillis());
        }, "t1");
        t1.start();
        LockSupport.unpark(t1);
        System.out.println(Thread.currentThread().getName()+"\t 通知t1...");
    }
}
