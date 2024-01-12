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

        byte[] ivBytes = new byte[16]; // 16�ֽڳ��ȵĳ�ʼ����
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);

        byte[] ciphertext = encryptSM4(keyBytes,ivBytes, plaintext.getBytes(StandardCharsets.UTF_8));
        System.out.println("Ciphertext: " + bytesToHexString(ciphertext));

        byte[] decryptedBytes = decryptSM4(keyBytes, ivBytes,ciphertext);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("Decrypted: " + decryptedText);
    }

    /**����
     * 2023/8/13-23:51
     * @author pengfulin
    */
    public static byte[] encryptSM4(byte[] keyBytes, byte[] ivBytes,byte[] plaintext) throws Exception {
        BlockCipher engine = new SM4Engine(); //SM4 �㷨�Ŀ���������
        //BufferedBlockCipher �� Bouncy Castle ���е�һ����װ�࣬����ʵ�ֿ������㷨�ļӽ��ܲ�������֧����䷽ʽ��
        // ��������Ǵ�����һ��ʹ�� CBC��Cipher Block Chaining��ģʽ�� BufferedBlockCipher ʵ�������� SM4Engine ��Ϊ��ײ�Ŀ��������档
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
        //KeyParameter �� Bouncy Castle �������ڴ�����Կ�������ࡣ���ｨ��һ�� KeyParameter ʵ������ keyBytes ��Ϊ��Կ��������
        KeyParameter keyParam = new KeyParameter(keyBytes);
        ParametersWithIV params = new ParametersWithIV(keyParam, ivBytes);
        //�����ʼ����������init �����ĵ�һ������ָ������ģʽ��true ��ʾ����ģʽ���ڶ�����������Կ����
        cipher.init(true, params);
        //����һ���ֽ����� output�����ڴ洢���ܺ�����ݡ����ݼ������������С������������㹻�Ŀռ䡣
        byte[] output = new byte[cipher.getOutputSize(plaintext.length)];
        //processBytes �������ڴ����������ݵ�һ���֡����ｫ������������ plaintext ��ȫ�����ݶ�������
        // �����ܺ�����ݴ洢�� output �����С�len ��¼�˴�����ֽ�����
        int len = cipher.processBytes(plaintext, 0, plaintext.length, output, 0);
        //doFinal �����ᴦ���������ݵ�ʣ�ಿ�֣��������ܺ�����ݴ洢�� output �����ʣ��λ�á�
        cipher.doFinal(output, len);
        return output;
    }

    /**����
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

    /**������ת16����
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
