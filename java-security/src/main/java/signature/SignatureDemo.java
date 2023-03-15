package signature;

import asymmetry.RSADemo;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.security.*;
import java.util.Map;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: 数字签名过程演示
 * @date 2021/10/23 13:32
 */
public class SignatureDemo {


    public static void main(String[] args) throws Exception {
        String algorithm = "RSA";
        String data="地势坤，君子以厚德载物";
        Map<String, Object> keys = RSADemo.getKey(algorithm);

        //生成签名：发送方
         //创建签名对象 : 签名算法由信息摘要算法和非对称加密算法构成
        Signature signature = Signature.getInstance("sha256withrsa");
         //初始化签名:传入私钥
//        Map<String, Object> replaceKey = RSADemo.getKey(algorithm);
//        keys.put("privateKey",replaceKey.get("privateKey")); //非法发送方
        signature.initSign((PrivateKey) keys.get("privateKey"));
         //传入原文：生成信息摘要
        signature.update(data.getBytes());
         //生成签名：
        byte[] sign = signature.sign();
        //使用base64解码...

        //校验签名：接收方
         //创建签名校验对象
        Signature signature1 = Signature.getInstance("sha256withrsa");
         //初始化签名：传入公钥
        signature1.initVerify((PublicKey) keys.get("publicKey"));
         //传入原文：生成信息摘要
//        data="天行健，君子以自强不息"; //篡改信息
        signature1.update(data.getBytes());
         //进行签名校验：原文信息摘要是否和签名解密后的信息摘要内容一致
         //使用base64编码...
        boolean verify = signature1.verify(sign);
        System.out.println("完成签名校验："+verify);
    }
}
