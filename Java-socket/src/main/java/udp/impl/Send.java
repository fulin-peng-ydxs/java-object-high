package udp.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: UDP编程发送端
 * @date 2021/10/2 21:04
 */
public class Send {

    /**
     * 说明：
     *
     * 01 程序自身不会监听端口，而是socket机制实现的
     *
     * 02 udp传输数据，不需要关闭socket对象，也不需要一直使用新的数据包
     *    如果将地址和端口等所有要传输的信息放在数据包中，则不用进行连接，无论对方时候准备好
     *    都会发出去，所以使用udp传输数据资源开销下，传输速度快
     *
     * 03 如果将发送的地址和端口放在socket中，则需要连接到接收端才可进行发送
     *    如果中间失去连接，则后面的发送也会失败
     */




    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        int num=0;
        while (true){
            try {
                num++;
                System.out.println("正在发送数据包.....");
                byte[] by = "hello,atguigu.com".getBytes();
                DatagramPacket dp = new DatagramPacket(by, 0, by.length,
                        InetAddress.getByName("192.168.20.0"), 10000);
                ds.send(dp);
                System.out.println("已发送数据包"+num+"次");

//                获取socket的，System.out.println(ds.getPort()+","+ds.getInetAddress());
//                System.out.println(ds.getLocalAddress()+","+ds.getLocalPort());
//
//                获取数据包的，System.out.println(dp.getAddress()+","+dp.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
