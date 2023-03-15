package socket;

import org.junit.Test;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
/**
 * @author PengFuLin
 * @description 阻塞式IO通信
 * @date 2022/5/1 18:44
 */
public class BlockingNIODemo {
    /*
     *使用 NIO 完成网络通信的三个核心：
     * 1. 通道（Channel）：负责连接
     * 	   java.nio.channels.Channel 接口：
     * 			|--SelectableChannel
     * 				|--SocketChannel
     * 				|--ServerSocketChannel
     * 				|--DatagramChannel
     *
     * 				|--Pipe.SinkChannel
     * 				|--Pipe.SourceChannel
     *
     * 2. 缓冲区（Buffer）：负责数据的存取
     *
     * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。
     * 用于监控 SelectableChannel 的 IO 状况
     */

    @Test
    public void client2() throws IOException, InterruptedException {
        //1. 获取通道:选择连接的ip和端口
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2.发送消息
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("客户端已启动：");
        int send=1;
        while(true){
            Thread.sleep(500);
            String content = new Date()+" 客户端2的消息为："+send;
            System.out.println("正在发送消息："+content);
            buf.put(content.getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            send++;
            if(send==30)
                break;
        }
        //3. 关闭通道
        sChannel.close();
    }


    @Test
    public void client1() throws IOException, InterruptedException {
        //1. 获取通道:选择连接的ip和端口
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2.发送消息
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("客户端已启动：");
        int send=1;
        while(true){
            Thread.sleep(500);
            String content = new Date()+" 客户端1的消息为："+send;
            System.out.println("正在发送消息："+content);
            buf.put(content.getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            int len = sChannel.read(buf);
            byte[] array = buf.array();
            buf.clear();
            System.out.println("客户端接收反馈："+new String(array,0,len));
            send++;
            if(send==10){
                //关闭通道的输出
                sChannel.shutdownOutput();
                break;
            }
        }
        int len = sChannel.read(buf);
        byte[] array = buf.array();
        System.out.println("客户端接收反馈："+new String(array,0,len));
        //3. 关闭通道
        sChannel.close();
    }

    //服务端
    @Test
    public void server() throws IOException {
        //1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2.绑定服务的端口和ip
        ssChannel.bind(new InetSocketAddress(9898));
        while (true){  //这里是单线层模式依次排队处理所有请求
            System.out.println("正在等待来连接..");
            //3.获取客户端连接的通道：
            SocketChannel sChannel = ssChannel.accept();
            //4. 接收客户端的数据
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int len; //消息长度
            //循环读取所有的内容：以1024个字节为单位
            while(true){
                System.out.println("正在等待接收消息..");
                //开始阻塞（在连接通道没有将数据进行写入buf时，一直会处于阻塞状态）
                //如果连接通道关闭了输出或者没有数据可读时，将返回-1
                len=sChannel.read(buf);
                if(len>0){
                    buf.flip();
                    byte[] array = buf.array();
                    System.out.println("服务端接收消息："+new String(array,0,len));
                    buf.clear();
                    buf.put("服务端已收到你的消息".getBytes());
                    buf.flip();
                    sChannel.write(buf);
                    buf.clear();
                }else{
                    System.out.println("客户端以关闭！");
                    buf.put("欢迎下次访问！".getBytes());
                    buf.flip();
                    sChannel.write(buf);
                    sChannel.close(); //关闭通道
                    break;
                }
            }
            sChannel.close();
        }
    }
}
