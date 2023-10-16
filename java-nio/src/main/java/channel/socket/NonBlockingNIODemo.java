package channel.socket;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
/**
 * @author PengFuLin
 * @description 非阻塞式IO通信
 * @date 2022/5/1 20:30
 */
public class NonBlockingNIODemo {

    //客户端
    @Test
    public void client1() throws IOException, InterruptedException {
        //1. 获取通道:选择连接的ip和端口
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2.发送消息
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("客户端已启动：");
        int send=1;
        while(true){
            Thread.sleep(2000);
            String content = new Date()+" 客户端1的消息为："+send;
            System.out.println("正在发送消息："+content);
            buf.put(content.getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();  //缓存置空
            int len = sChannel.read(buf);
            byte[] array = buf.array();  //buf值转换为字节数组
            buf.clear(); //缓存置空
            System.out.println("客户端接收反馈："+new String(array,0,len));
            send++;
            if(send==5){
                //关闭通道的输出：服务器端也就作为一个读请求，不过通道将读取不到数据
                sChannel.shutdownOutput();
                break;
            }
        }
        int len = sChannel.read(buf);
        byte[] array = buf.array();
        System.out.println("客户端接收反馈："+new String(array,0,len));
        //3. 关闭通道
        sChannel.close();
        System.out.println("客户端通道已关闭");
    }



    //服务端
    @Test
    public void server() throws IOException{
        //获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //切换服务通道为非阻塞模式
        ssChannel.configureBlocking(false);
        //绑定服务的端口和ip
        ssChannel.bind(new InetSocketAddress(9898));
        //创建选择器
        Selector selector = Selector.open();
        //选择器绑定事件:监听请求的“接收”事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("正在等待来连接..");
        while(selector.select()>0){ //会进行阻塞获取监听的事件已经就绪的通道，并将其通道放入selectedKeys中
            System.out.println("已监控到可用通道..");
            //获取监听到的”接收”事件的类型
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                System.out.println("已获取到监听事件..");
                SelectionKey selectionKey = iterator.next();
                //用多路复用机制处理这些不同类型的请求：
                //“接收”事件是否就绪
                if(selectionKey.isAcceptable()){
                    System.out.println("请求开始接收..");
                    //开始获取请求客户端通道
                    SocketChannel accept = ssChannel.accept();
                    //切换非阻塞模式
                    accept.configureBlocking(false);
                    //选择器绑定事件：监听请求的读事件
                    accept.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    //接收客户端的数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len=0; //消息长度
                    //循环读取所有的内容：以1024个字节为单位
                    while(channel.isOpen()){
                        System.out.println("正在接收消息..");
                        len=channel.read(buf);
                        System.out.println(len);
                        if(len>0){
                            buf.flip();
                            byte[] array = buf.array();
                            System.out.println("服务端接收消息："+new String(array,0,len));
                            buf.clear();
                            buf.put("服务端已收到你的消息".getBytes());
                            buf.flip();
                            channel.write(buf);
                            buf.clear();
                        }else{
                            break;
                        }
                    }
                    if(len==-1){
                        System.out.println("客户端以关闭！");
                        buf.put("欢迎下次访问！".getBytes());
                        buf.flip();
                        channel.write(buf);
                        channel.close(); //关闭通道
                    }
                }
                System.out.println("事件已处理..");
                iterator.remove();
            }
        }
    }
}
