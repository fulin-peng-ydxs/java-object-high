package security.algorithm.equals;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.SecureRandom;
import java.security.Security;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SM4Security {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String keyString = "0123456789abcdef";
        byte[] keyBytes = keyString.getBytes(StandardCharsets.UTF_8);

        String plaintext = "This is a test message.";

        byte[] ivBytes = new byte[16]; // 16字节长度的初始向量
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);

        byte[] ciphertext = encryptSM4(keyBytes,ivBytes, plaintext.getBytes(StandardCharsets.UTF_8));
        System.out.println("Ciphertext: " + bytesToHexString(ciphertext));

        byte[] decryptedBytes = decryptSM4(keyBytes, ivBytes,ciphertext);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Decrypted: " + decryptedText);
    }

    /**加密
     * 2023/8/13-23:51
     * @author pengfulin
    */
    public static byte[] encryptSM4(byte[] keyBytes, byte[] ivBytes,byte[] plaintext) throws Exception {
        BlockCipher engine = new SM4Engine(); //SM4 算法的块密码引擎
        //BufferedBlockCipher 是 Bouncy Castle 库中的一个封装类，用于实现块密码算法的加解密操作，并支持填充方式。
        // 在这里，我们创建了一个使用 CBC（Cipher Block Chaining）模式的 BufferedBlockCipher 实例，并将 SM4Engine 作为其底层的块密码引擎。
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
        //KeyParameter 是 Bouncy Castle 库中用于传递密钥参数的类。这里建了一个 KeyParameter 实例，将 keyBytes 作为密钥参数传入
        KeyParameter keyParam = new KeyParameter(keyBytes);
        ParametersWithIV params = new ParametersWithIV(keyParam, ivBytes);
        //代码初始化加密器。init 方法的第一个参数指定操作模式，true 表示加密模式。第二个参数是密钥参数
        cipher.init(true, params);
        //创建一个字节数组 output，用于存储加密后的数据。根据加密器的输出大小，这里分配了足够的空间。
        byte[] output = new byte[cipher.getOutputSize(plaintext.length)];
        //processBytes 方法用于处理输入数据的一部分。这里将输入明文数据 plaintext 的全部内容都处理，并
        // 将加密后的数据存储到 output 数组中。len 记录了处理的字节数。
        int len = cipher.processBytes(plaintext, 0, plaintext.length, output, 0);
        //doFinal 方法会处理输入数据的剩余部分，并将加密后的数据存储到 output 数组的剩余位置。
        cipher.doFinal(output, len);
        return output;
    }

    /**解密
     * 2023/8/13-23:51
     * @author pengfulin
    */
    public static byte[] decryptSM4(byte[] keyBytes,byte[] ivBytes, byte[] ciphertext) throws Exception {
        BlockCipher engine = new SM4Engine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
        KeyParameter keyParam = new KeyParameter(keyBytes);
        ParametersWithIV params = new ParametersWithIV(keyParam, ivBytes);
        cipher.init(false, params);
        byte[] output = new byte[cipher.getOutputSize(ciphertext.length)];
        int len = cipher.processBytes(ciphertext, 0, ciphertext.length, output, 0);
        len += cipher.doFinal(output, len);

        return Arrays.copyOf(output, len);
    }

    /**二进制转16进制
     * 2023/8/13-23:51
     * @author pengfulin
    */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
