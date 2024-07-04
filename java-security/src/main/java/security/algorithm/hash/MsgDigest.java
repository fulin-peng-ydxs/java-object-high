package security.algorithm.hash;

import security.utils.Base64Utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 消息摘要算法演示
 * @date 2021/10/23 11:40
 */
public class MsgDigest {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        //原文
        String data="xhd123@Admin";
        //消息摘要算法：
        String msgDigest="MD5";
//        msgDigest="SHA-1"; //SHA-256、SHA-512
        //获取消息摘要对象
        MessageDigest md5 = MessageDigest.getInstance(msgDigest);
        //获取原文的消息摘要
        byte[] digest = md5.digest(data.getBytes());
        //消息摘一般要转变成16进制:没有用base64进行编码
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            String toHexString = Integer.toHexString(b & 0xff);
            if(toHexString.length()==1){
                toHexString="0"+toHexString;
            }
            builder.append(toHexString);
        }
        System.out.println(builder);
        //使用base64进行消息摘要翻译
        String encode = Base64Utils.encode(builder.toString().getBytes());
        System.out.println(encode);
    }
}
