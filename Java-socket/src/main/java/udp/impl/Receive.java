package udp.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: UDP编程接收端
 * @date 2021/10/2 21:09
 */
public class Receive {
    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                byte[] by = new byte[1024];
                DatagramPacket dp = new DatagramPacket(by, by.length);
                ds.receive(dp);
                String str = new String(dp.getData(), 0, dp.getLength());
                System.out.println(str + "--" + dp.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}



