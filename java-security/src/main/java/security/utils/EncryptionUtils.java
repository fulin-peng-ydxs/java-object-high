package security.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * ���ܹ�����
 *
 * @author fulin-peng
 * 2024-08-19  17:33
 */
public class EncryptionUtils {

     /**
      * �ԳƼ���
      * 2024/8/20 ����9:27
      * @author fulin-peng
      * @param plainText �������ı�
      * @param encryptKey ����key des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      * @param transformation �����㷨
      * @param cbcInit �������� des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      */
     public static String symmetryEncrypt(final String plainText,String encryptKey,String transformation,String cbcInit){
         try {
             return Base64Utils.encode(symmetryAlgorithm(plainText.getBytes(),encryptKey,transformation,cbcInit,Cipher.ENCRYPT_MODE));
         } catch (Exception e) {
             throw new RuntimeException("�ԳƼ����쳣",e);
         }
     }
     
     /**
      * �Գƽ���
      * 2024/8/20 ����9:54 
      * @author fulin-peng
      * @param encryptedText ���������� base64��
      * @param decryptKey ����key des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      * @param transformation �����㷨
      * @param cbcInit �������� des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      */
     public static String symmetryDecrypt(final String encryptedText,String decryptKey,String transformation,String cbcInit){
         try {
             return new String(symmetryAlgorithm(Base64Utils.decodeData(encryptedText),decryptKey,transformation,cbcInit,Cipher.DECRYPT_MODE));
         } catch (Exception e) {
             throw new RuntimeException("�Գƽ����쳣",e);
         }
     }
     
     /**
      * �Գ��㷨
      * 2024/8/20 ����9:58
      * @param data ���ӽ����ֽ�����
      * @param key ���ӽ���key des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      * @param transformation �ӽ����㷨
      * @param cbcInit �ӽ������� des�㷨�³���Ϊ8���ֽڣ�aes�㷨�³���Ϊ16���ֽ�
      * @param type ���ܻ���� 1Ϊ���� ��2Ϊ����
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
             //CBCģʽ����Ҫ�����ʼ����
             IvParameterSpec ivParameterSpec = new IvParameterSpec(cbcInit.getBytes());
             cipher.init(type,secretKey,ivParameterSpec);
         }else cipher.init(type,secretKey);
         return cipher.doFinal(data);
     }

}
