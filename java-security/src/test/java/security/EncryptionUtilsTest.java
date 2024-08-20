package security;

import org.junit.Test;
import security.utils.EncryptionUtils;

/**
 * @author fulin-peng
 * 2024-08-20  10:12
 */
public class EncryptionUtilsTest {


    @Test
    public void testEncrypt() {
        String data="硅谷"; //原文数据
        String transformation="DES/CBC/PKCS5Padding";  //加密算法
        // DES加密算法,key、init的大小必须是8个字节
        String key="12345678";
        String init="12367835";
        String encrypt = EncryptionUtils.symmetryEncrypt(data, key, transformation, init);
        String decrypt = EncryptionUtils.symmetryDecrypt(encrypt, key, transformation, init);
        System.out.println(encrypt+"----"+decrypt);
        System.out.println("-===================================================================================-");
        // AES加密算法,key、init的大小必须是16个字节
        key="1234567891123456";
        init="1234567891127756";
        transformation="AES/CBC/PKCS5Padding";  //加密算法
        encrypt = EncryptionUtils.symmetryEncrypt(data, key, transformation, init);
        decrypt = EncryptionUtils.symmetryDecrypt(encrypt, key, transformation, init);
        System.out.println(encrypt+"----"+decrypt);
    }
}
