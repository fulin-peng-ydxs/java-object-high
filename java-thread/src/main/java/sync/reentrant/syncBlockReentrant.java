package sync.reentrant;

/**
 * 在一个synchronized修饰的方法或代码块的内部
 * 调用本类的其他synchronized修饰的方法或代码块时，是永远可以得到锁的
 */

public class syncBlockReentrant {

    static Object objectLockA = new Object();

    public static void m1(){
        new Thread(() -> {
            synchronized (objectLockA){
                System.out.println(Thread.currentThread().getName()+"\t"+"------外层调用");
                synchronized (objectLockA){
                    System.out.println(Thread.currentThread().getName()+"\t"+"------中层调用");
                    synchronized (objectLockA)
                    {
                        System.out.println(Thread.currentThread().getName()+"\t"+"------内层调用");
                    }
                }
            }
        },"t1").start();

    }
    public static void main(String[] args) {
        m1();
    }
}


