package interfaces.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: jdk动态代理实现
 * @date 2021/10/4 0:38
 */
public class ProxyImpl {


    public static void main(String[] args) {
        //创建被代理被对象
        Jordan jordan = new Jordan();
        //为被代理对象创建代理对象
        Person proxyInstance =(Person)ProxyFactory.getProxyInstance(jordan);
        //调用代理对象方法，提供对调用代理对象的方法的控制
        proxyInstance.show("jordan");
        System.out.println(proxyInstance.input());
    }
}

//代理接口
interface Person{
    //介绍
    void show(String name);
    //说话
    String input();
}

//被代理类
class Jordan implements  Person{
    @Override
    public void show(String name) {
        System.out.println("我的名字是:" + name);
    }
    @Override
    public String input() {
        return "我非常热爱生活";
    }
}


//代理类工厂
class ProxyFactory{
    //创建动态代理对象 ：参数为需要被代理的对象，返回值为根据被代理的对象所创建的代理对象
    public static Object getProxyInstance(Object proxySource){
        //创建动态代理处理者：动态代理对象功能逻辑的实现者
        ProxyInvocationHandler handler = new ProxyInvocationHandler();
        handler.bindProxySource(proxySource);
        //使用动态代理工具类，为被代理对象生成动态代理对象：
        // 参数一，提供类加载器，参数二，提供动态代理接口（生成的动态代理类就是此接口的实现类），参数三，提供动态代理处理。
        // 返回值为动态代理对象
        return java.lang.reflect.Proxy.newProxyInstance(proxySource.getClass().getClassLoader(),
                proxySource.getClass().getInterfaces(),handler);
    }
}


//代理类功能逻辑实现
class ProxyInvocationHandler implements InvocationHandler {
    // 被代理对象
    private Object proxySource;
    //绑定被代理对象
    public void bindProxySource(Object proxySource){ this.proxySource=proxySource; }

    @Override
    //动态代理对象功能逻辑实现： 参数一，为代理对象，参数二，为代理对象方法，参数三，为代理对象方法传入的参数
    //返回值为被代理的对象所调用的方法所返回的值
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //前置逻辑
        Object invoke = method.invoke(proxySource, args);
        //由于代理对象实现了代理类共同的接口方法。所以可以利用反射原理来调用被代理对象的对应方法
        //后置逻辑
        return invoke;
    }
}
