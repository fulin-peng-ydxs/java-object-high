package security.algorithm.equals;

import security.utils.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: AES对称加密演示
 * @date 2021/10/21 23:56
 */
public class AesSecurity {


    public static void main(String[] args) throws Exception {
        // AES加密算法,key的大小必须16个字节\24个字节\32个字节,分别对应AES-128、AES-192、AES-256 加密算法
        String key="1234567891123456";
        String transformation="AES";  //加密算法
        String algorithm="AES"; //密钥算法
        String data="硅谷"; //原文数据
        String encryptDes = encryptAes(data, key, transformation, algorithm);
        System.out.println("加密数据："+encryptDes);
        String decryptDes = decryptAes(encryptDes, key, transformation, algorithm);
        System.out.println("解密数据："+decryptDes);
    }


    /**
     * @Author PengFuLin
     * @Description  加密数据
     * @Date 0:00 2021/10/22
     * @Param [data：原文, key：密钥, transformation：加密算法, algorithm：密钥算法]
     * @return 密文
     **/
    private  static String encryptAes(String data,String key,String transformation,String algorithm) throws Exception {
        //获取安全对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建加密规则
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), algorithm);
        //使用加密功能
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        //进行加密
        byte[] bytes = cipher.doFinal(data.getBytes());
        //使用base64对加密数据进行翻译
        return Base64Utils.encode(bytes);
    }


    /**
     * @Author PengFuLin
     * @Description  解密数据
     * @Date 0:00 2021/10/22
     * @Param [data：原文, key：密钥, transformation：解密算法, algorithm：密钥算法]
     * @return 原文
     **/
    private static String decryptAes(String data,String key,String transformation,String algorithm) throws Exception{
        //获取安全对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建解密规则
        SecretKey secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        //使用解密功能
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        //进行解密:需要先将可读性加密数据解释原始加密数据
        byte[] bytes = cipher.doFinal(Base64Utils.decodeData(data));
        return new String(bytes);
    }

}
