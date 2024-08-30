package proxy.jdk.object;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


// 主类
public class Proxy_newProxyInstance{

    public static void main(String[] args) {
//        InvocationHandler 实现：定义了一个 InvocationHandler，其 invoke 方法将处理代理对象上调用的所有方法。在这个例子中，每次调用 myMethod 时，都会输出 "Invoked method: myMethod"。
//        Proxy.newProxyInstance 创建代理对象：
//        ClassLoader：用 MyInterface 的类加载器来加载代理类。
//        interfaces：指定代理类需要实现的接口，这里是 MyInterface。
//        handler：传入自定义的 InvocationHandler 实现，用于定义方法调用时的行为。
//        调用代理对象的方法：当调用 proxyInstance.myMethod() 时，它会被转发到 InvocationHandler 的 invoke 方法中进行处理。


        // 创建一个 InvocationHandler 实现，用于定义代理对象的方法行为
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("Invoked method: " + method.getName());
                return null; // 方法返回值
            }
        };

        // 使用 Proxy.newProxyInstance 创建代理对象
        MyInterface proxyInstance = (MyInterface) Proxy.newProxyInstance(
            MyInterface.class.getClassLoader(), // 类加载器
            new Class<?>[]{MyInterface.class},  // 代理类需要实现的接口
            handler                             // 方法调用处理器
        );

        // 调用代理对象的方法
        proxyInstance.myMethod(); // 输出: Invoked method: myMethod
    }
}
