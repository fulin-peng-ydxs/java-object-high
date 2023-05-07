package security.algorithm.asymmetry;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author PengFuLin
 * @version 1.0
 * @description: RSA非对称加密算法演示
 * @date 2021/10/23 12:23
 */
public class RSADemo {

    public static void main(String[] args) throws Exception {

        //原文
        String data="天行健，君子以自强不息";
        //加密算法
        String algorithm = "RSA";
        //获取密钥
        Map<String, Object> keys = getKey(algorithm);
        String aPrivate = (String) keys.get("private");
        String aPublic = (String) keys.get("public");
        System.out.println("公钥："+ aPublic);
        System.out.println("私钥："+ aPrivate);

        //举例：

        //使用公钥加密===================》

        //创建安全对象
        Cipher cipher = Cipher.getInstance(algorithm);
        //使用加密功能
        cipher.init(Cipher.ENCRYPT_MODE, (Key) keys.get("publicKey"));
        //对原文进行加密
        byte[] bytes = cipher.doFinal(data.getBytes());
        String encode = Base64.encode(bytes);
        System.out.println("密文：" + encode);

        //使用私钥解密《============================

        //创建安全对象
        Cipher cipher1 = Cipher.getInstance(algorithm);
        //使用解密功能
        cipher1.init(Cipher.DECRYPT_MODE, (Key) keys.get("privateKey"));
        //对密文进行解密
        byte[] bytes1 = cipher1.doFinal(Base64.decode(encode.getBytes()));
        System.out.println("原文："+new String(bytes1));
    }

    public  static Map<String,Object> getKey(String algorithm) throws NoSuchAlgorithmException {
        //加密算法
        String algorithms=algorithm;
        //创建密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithms);
        //生成公钥钥和私钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //获取公钥
        PublicKey aPublic = keyPair.getPublic();
        byte[] aPublicEncoded = aPublic.getEncoded(); //公钥字节数据
        //获取私钥
        PrivateKey aPrivate = keyPair.getPrivate();
        byte[] aPrivateEncoded = aPrivate.getEncoded(); //私钥字节数据
        //使用base64将密钥字节数据进行翻译
        String publicCode = Base64.encode(aPublicEncoded);
        String privateCode = Base64.encode(aPrivateEncoded);
        HashMap<String, Object> mapKey = new HashMap<>();
        mapKey.put("public",publicCode);
        mapKey.put("publicKey",aPublic);
        mapKey.put("private",privateCode);
        mapKey.put("privateKey",aPrivate);
        return mapKey;
    }
}
