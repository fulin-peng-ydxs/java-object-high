package notice.support;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author PengFuLin
 * @description LockSupport类支持的线程通信机制
 * @date 2022/4/24 12:06
 */
public class LockSupportDemo2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(5L); }catch (Exception e) {e.printStackTrace();}
            System.out.println(Thread.currentThread().getName() + "\t ----begi"+System.currentTimeMillis());
            LockSupport.park();//阻塞当前线程
            System.out.println(Thread.currentThread().getName() + "\t ----被唤醒"+System.currentTimeMillis());
        }, "t1");
        t1.start();

        try { TimeUnit.SECONDS.sleep(1); }catch (Exception e) {e.printStackTrace();}
        LockSupport.unpark(t1);
        System.out.println(Thread.currentThread().getName()+"\t 通知t1...");
    }
}
