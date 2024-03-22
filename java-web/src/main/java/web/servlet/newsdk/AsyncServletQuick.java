package web.servlet.newsdk;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: Servlet3.0-异步请求演示
 * @date 2021/11/22 11:14
 */

//1、支持异步处理asyncSupported=true
@WebServlet(value="/async",asyncSupported=true)
public class AsyncServletQuick extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("主线程开始。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
        //2、开启异步模式
        AsyncContext startAsync = req.startAsync();
        //3、业务逻辑执行异步处理;
        startAsync.start(() -> {
            try {
                System.out.println("副线程开始。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
                sayHello();
                //获取到异步上下文或直接使用startAsync
                //AsyncContext asyncContext = req.getAsyncContext();
                // 获取响应
                ServletResponse response = startAsync.getResponse();
                response.getWriter().write("hello async...");
                System.out.println("副线程结束。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                // 在所有操作完成后，最后调用complete()
                startAsync.complete();
            }
        });
        System.out.println("主线程结束。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
    }

    public void sayHello() throws Exception{
        System.out.println(Thread.currentThread()+" processing...");
        Thread.sleep(3000);
    }
}
