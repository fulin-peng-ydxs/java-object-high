package sync.reentrant;

/**
 * 在一个synchronized修饰的方法或代码块的内部
 * 调用本类的其他synchronized修饰的方法或代码块时，是永远可以得到锁的
 */
public class syncMethodReentrant {

        public synchronized void m1(){
                System.out.println("=====外层");
                m2();
        }

        public synchronized void m2() {
                System.out.println("=====中层");
                m3();
        }

        public synchronized void m3(){
                System.out.println("=====内层");
        }

        public static void main(String[] args) {
                new syncMethodReentrant().m1();
        }
}


