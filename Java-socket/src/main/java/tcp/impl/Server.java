package tcp.impl;

import com.sun.deploy.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: TCP编程服务端
 * @date 2021/10/2 17:06
 */
public class Server {





    //请求响应
    public static void write(OutputStream netOut, Socket socket ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String info = scanner.next();
        netOut.write(info.getBytes());
        //关闭响应流，完成响应
        socket.shutdownOutput();
        //关闭连接对象
        socket.close();
    }

    //请求获取
    public  static Object reade(InputStream netInput,Socket socket ) throws IOException {
        int len=0;
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while((len=netInput.read(bytes))!=-1){
            //如果客户端没有关闭请求流，且流中没有数据被写入时，则会进行阻塞
            byteArrayOutputStream.write(bytes,0,len);
        }
        return byteArrayOutputStream.toString();
    }



    public static void server(ServerSocket serverSocket) throws IOException, InterruptedException {
        System.out.println("服务端处理：");
        while(true){
            //开始监听连接请求，获取连接对象 ：当监听到连接的请求时，就会开始执行下面的程序
            Socket socket = serverSocket.accept();
            //获取请求流和响应流，用于信息传输
            InputStream netInput =socket.getInputStream();
            OutputStream netOut=socket.getOutputStream();
            //获取请求
            String result = (String) reade(netInput, socket);
            System.out.println("***************************************************");
            System.out.println("接受请求信息:");
            System.out.println(result);
            //进行响应
            System.out.println("********************************************");
            System.out.println("进行结果响应:");
            write(netOut,socket);
            if(result!=null&&result.equals("closeServer")){
                System.out.println("正在关闭服务器.....");
                Thread.sleep(1000);
                //关闭监听请求连接对象 ：停止服务
                serverSocket.close();
                System.out.println("服务器已关闭.....");
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //启动程序，创建请求连接监听对象，绑定程序监听端口
        ServerSocket serverSocket = new ServerSocket(8088);
        System.out.println("服务端已启动：");
        //通过监听对象，监听请求，完成请求的响应
        server(serverSocket);
    }
}
