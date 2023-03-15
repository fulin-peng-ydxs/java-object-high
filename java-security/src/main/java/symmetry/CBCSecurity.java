package symmetry;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: CBC加密模式演示
 * @date 2021/10/21 23:56
 */
public class CBCSecurity {


    public static void main(String[] args) throws Exception {
        // DES加密算法,key的大小必须是8个字节
        String key="12345678";
        String transformation="DES/CBC/PKCS5Padding";  //加密算法
        String algorithm="DES"; //密钥算法
        String data="硅谷"; //原文数据
        String encryptDes = encryptDes(data, key, transformation, algorithm,"12367835");
        System.out.println("加密数据："+encryptDes);
        String decryptDes = decryptDes(encryptDes, key, transformation, algorithm,"12367835");
        System.out.println("解密数据："+decryptDes);
    }
    
    
    /**
     * @Author PengFuLin
     * @Description  加密数据
     * @Date 0:00 2021/10/22
     * @Param [data：原文, key：密钥, transformation：加密算法, algorithm：密钥算法]
     * @return 密文
     **/
    private  static String encryptDes(String data,String key,String transformation,String algorithm,String init) throws Exception {
        //获取安全对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建加密规则
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), algorithm);
        //CBC模式，需要加入初始向量 des算法下长度为8个字节，aes算法下长度为16个字节
        IvParameterSpec ivParameterSpec = new IvParameterSpec(init.getBytes());
        //使用加密功能
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);
        //进行加密
        byte[] bytes = cipher.doFinal(data.getBytes());
        //使用base64对加密数据进行翻译
        return Base64.encode(bytes);
    }


    /**
     * @Author PengFuLin
     * @Description  解密数据
     * @Date 0:00 2021/10/22
     * @Param [data：原文, key：密钥, transformation：解密算法, algorithm：密钥算法]
     * @return 原文
     **/
    private static String decryptDes(String data,String key,String transformation,String algorithm,String init) throws Exception{
        //获取安全对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建解密规则
        SecretKey secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        //CBC模式 添加解密向量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(init.getBytes());
        //使用解密功能
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);
        //进行解密:需要先将可读性加密数据解释成原始加密数据
        byte[] bytes = cipher.doFinal(Base64.decode(data.getBytes()));
        return new String(bytes);
    }






}
