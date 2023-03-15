package tcp.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: TCP编程客户端
 * @date 2021/10/2 17:05
 */
public class Client {


    //请求发送
    public static void write(OutputStream netOut, Socket socket ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String info = scanner.next();
        netOut.write(info.getBytes());
        //关闭请求流，发送请求
        socket.shutdownOutput();
    }
    //响应读取
    public  static Object reade(InputStream netInput,Socket socket ) throws IOException {
        int len=0;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while((len=netInput.read(bytes))!=-1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        return byteArrayOutputStream.toString();
    }


    public static void client() throws IOException {
        System.out.println("客户端访问:");
        //请求交互：一次循环就是一次请求的发送和响应
        while(true){
            //创建socket连接对象，开始建立与服务端的连接：绑定服务端的ip和端口
            Socket socket = new Socket("localhost", 8088);
            //获取请求流和响应流，用于信息传输
            InputStream netInput =socket.getInputStream();;
            OutputStream netOut=socket.getOutputStream();
            //发送请求
            System.out.println("**********************************");
            System.out.println("请输入请求内容:");
            write(netOut,socket);
            //获取响应
            String result = (String) reade(netInput, socket);
            System.out.println("***************************************");
            System.out.println("收到请求，返回信息：");
            System.out.println(result);
            //关闭socket连接对象,结束请求
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        client();
    }

}
