package security.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

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


     /**
      * 非对称加密
      * @param plainText 待加密明文
      * @param publicKey 公钥
      * @param transformation 加密算法
      * 2024/11/18 下午4:06
      * @author fulin-peng
      */
     public static String asymmetricEncrypt(final String plainText,String publicKey,String transformation) throws Exception{
         Cipher cipher = Cipher.getInstance(transformation);
         cipher.init(Cipher.ENCRYPT_MODE, KeyFactory.getInstance(transformation)
                 .generatePublic(new X509EncodedKeySpec(Base64Utils.decodeData(publicKey))));
         byte[] bytes = cipher.doFinal(plainText.getBytes());
         return Base64Utils.encode(bytes);
     }

     /**
      * 非对称解密
      * 2024/11/18 下午4:37
      * @author fulin-peng
      */
     public static String asymmetricDecrypt(final String encryptedText,String privateKey,String transformation) throws Exception{
         Cipher cipher = Cipher.getInstance(transformation);
         cipher.init(Cipher.DECRYPT_MODE, KeyFactory.getInstance(transformation)
                 .generatePrivate(new PKCS8EncodedKeySpec(Base64Utils.decodeData(privateKey))));
         byte[] bytes = cipher.doFinal(Base64Utils.decodeData(encryptedText));
         return new String(bytes);
     }

     /**
      * 生成秘钥对
      * 2024/11/18 下午4:28
      * @author fulin-peng
      */
     public  static Map<String,Object> getKey(String algorithm) throws NoSuchAlgorithmException {
         //创建密钥对生成器
         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
         //生成公钥钥和私钥对
         KeyPair keyPair = keyPairGenerator.generateKeyPair();
         //获取公钥
         PublicKey aPublic = keyPair.getPublic();
         byte[] aPublicEncoded = aPublic.getEncoded(); //公钥字节数据
         //获取私钥
         PrivateKey aPrivate = keyPair.getPrivate();
         byte[] aPrivateEncoded = aPrivate.getEncoded(); //私钥字节数据
         //使用base64将密钥字节数据进行翻译
         String publicCode = Base64Utils.encode(aPublicEncoded);
         String privateCode = Base64Utils.encode(aPrivateEncoded);
         HashMap<String, Object> mapKey = new HashMap<>();
         mapKey.put("publicCode",publicCode);
         mapKey.put("publicKey",aPublic);
         mapKey.put("privateCode",privateCode);
         mapKey.put("privateKey",aPrivate);
         return mapKey;
     }
}
