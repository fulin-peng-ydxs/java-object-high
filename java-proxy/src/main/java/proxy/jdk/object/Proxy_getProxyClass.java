package proxy.jdk.object;

import java.lang.reflect.*;

interface MyInterface {
    void myMethod();
}

public class Proxy_getProxyClass {

    public static void main(String[] args) {
//        Proxy.getProxyClass(): 创建了一个代理类，该代理类实现了 MyInterface 接口。
//        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);: 获取代理类的构造方法，该构造方法接受一个 InvocationHandler 参数。
//        constructor.newInstance(...): 使用构造方法创建代理类的实例，传入自定义的 InvocationHandler 实现。
//        proxyInstance.myMethod(): 调用代理实例的方法时，由 InvocationHandler 的 invoke 方法处理。
        try {
            // 获取代理类的 Class 对象
            Class<?> proxyClass = Proxy.getProxyClass(
                    MyInterface.class.getClassLoader(),
                    MyInterface.class
            );

            // 通过反射获取代理类的构造方法
            Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);

            // 使用构造方法创建代理类的实例
            MyInterface proxyInstance = (MyInterface) constructor.newInstance(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("Invoked method: " + method.getName());
                    return null;
                }
            });
            // 调用代理实例的方法
            proxyInstance.myMethod(); // 输出：Invoked method: myMethod
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
