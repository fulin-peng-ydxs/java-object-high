package notice.sync;

import java.util.concurrent.TimeUnit;
/**
 * @auther zzyy
 * @create 2022-01-20 16:14
 */
public class Synchronized{


    public static void main(String[] args )
    {
        syncWaitNotify();
    }


    private static void syncWaitNotify()
    {
        Object objectLock = new Object();

        new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName()+"\t ----come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t ----������");
            }
        },"t1").start();

        //��ͣ�������߳�
        //try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock){
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t ----����֪ͨ");
            }
        },"t2").start();
    }
}
