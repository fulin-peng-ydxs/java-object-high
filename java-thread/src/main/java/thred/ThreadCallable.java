package thred;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadCallable {
    public static void main(String[] args) {

        FutureTask<Integer> futureTask = new FutureTask<>(new CallableImpl());
        new Thread(futureTask).start();
        Integer value = null;
        try {
            value = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(value);
    }
}

class CallableImpl implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("-----thred.CallableImpl");
        return 200;
    }
}
