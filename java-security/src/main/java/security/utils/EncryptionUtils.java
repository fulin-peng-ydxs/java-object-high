package security.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 *
 * @author fulin-peng
 * 2024-08-19  17:33
 */
public class EncryptionUtils {

     /**
      * 对称加密
      * 2024/8/20 上午9:27
      * @author fulin-peng
      * @param plainText 待加密文本
      * @param encryptKey 加密key des算法下长度为8个字节，aes算法下长度为16个字节
      * @param transformation 加密算法
      * @param cbcInit 加密向量 des算法下长度为8个字节，aes算法下长度为16个字节
      */
     public static String symmetryEncrypt(final String plainText,String encryptKey,String transformation,String cbcInit){
         try {
             return Base64Utils.encode(symmetryAlgorithm(plainText.getBytes(),encryptKey,transformation,cbcInit,Cipher.ENCRYPT_MODE));
         } catch (Exception e) {
             throw new RuntimeException("对称加密异常",e);
         }
     }
     
     /**
      * 对称解密
      * 2024/8/20 上午9:54 
      * @author fulin-peng
      * @param encryptedText 待解密密文 base64串
      * @param decryptKey 解密key des算法下长度为8个字节，aes算法下长度为16个字节
      * @param transformation 解密算法
      * @param cbcInit 解密向量 des算法下长度为8个字节，aes算法下长度为16个字节
      */
     public static String symmetryDecrypt(final String encryptedText,String decryptKey,String transformation,String cbcInit){
         try {
             return new String(symmetryAlgorithm(Base64Utils.decodeData(encryptedText),decryptKey,transformation,cbcInit,Cipher.DECRYPT_MODE));
         } catch (Exception e) {
             throw new RuntimeException("对称解密异常",e);
         }
     }
     
     /**
      * 对称算法
      * 2024/8/20 上午9:58
      * @param data 待加解密字节数组
      * @param key 待加解密key des算法下长度为8个字节，aes算法下长度为16个字节
      * @param transformation 加解密算法
      * @param cbcInit 加解密向量 des算法下长度为8个字节，aes算法下长度为16个字节
      * @param type 加密或解密 1为加密 、2为解密
      * @author fulin-peng
      */
     public static byte[] symmetryAlgorithm(final byte[] data,String key,String transformation,String cbcInit,int type) throws Exception{
         String algorithm=null;
         if (transformation.contains("/")) {
             algorithm=transformation.split("/")[0];
         }else {
             algorithm=transformation;
         }
         Cipher cipher = Cipher.getInstance(transformation);
         SecretKey secretKey = new SecretKeySpec(key.getBytes(), algorithm);
         if(cbcInit!=null&&transformation.contains("CBC")){
             //CBC模式，需要加入初始向量
             IvParameterSpec ivParameterSpec = new IvParameterSpec(cbcInit.getBytes());
             cipher.init(type,secretKey,ivParameterSpec);
         }else cipher.init(type,secretKey);
         return cipher.doFinal(data);
     }

}
