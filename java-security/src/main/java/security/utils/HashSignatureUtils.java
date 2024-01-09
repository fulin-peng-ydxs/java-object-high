package security.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**hash签名工具
 * 2023/8/10-22:32
 * @author pengfulin
*/
public class HashSignatureUtils {

    public enum SignatureHash {
        SHA_256,
        MD5,
        SM3
    }

    /**生成签名：通用方式
     * 2023/8/10-23:45
     * @author pengfulin
    */
    public static Map<String,String> doGetCommonSignature(List<String> signatureData,String signatureTemplate,SignatureHash signatureHash) throws Exception{
        String signatureSource = String.format(signatureTemplate, signatureData);
        Map<String,String> signatureResult=null;
        if(signatureHash==SignatureHash.SHA_256){
            signatureResult=doGetSimpleSha256Signature(signatureSource);
        }else if(signatureHash==SignatureHash.SM3){
            signatureResult=doGetSimpleSM3Signature(signatureSource);
        }else{
            return null;
        }
        return signatureResult;
    }


    /**签名生成:通用sha256算法
     * 2023/6/25 0025-11:57
     * @author pengfulin
     */
    public static Map<String,String> doGetSimpleSha256Signature(String token) throws Exception{
        long now = System.currentTimeMillis();
        String timestamp = Long.toString(now / 1000L);  //时间戳
        String nonce = Long.toHexString(now) + "-" + Long.toHexString((long) Math.floor(Math.random() * 0xFFFFFF));  //签名盐
        String signature = toSHA256(timestamp + token + nonce + timestamp);
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("signature",signature);
        map.put("nonce",nonce);
        return map;
    }

    /**生成sha256算法hash
     * 2023/8/10-22:39
     * @author pengfulin
    */
    public static String toSHA256(String str) throws Exception {
        MessageDigest messageDigest;
        String encodeStr = "";
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        encodeStr = bytesToHex(messageDigest.digest());
        return encodeStr;
    }



    /**签名生成:通用sm3算法
     * 2023/6/25 0025-15:06
     * @author pengfulin
    */
    public static Map<String,String> doGetSimpleSM3Signature(String appSecret) {
        long now = System.currentTimeMillis();
        String timestamp = Long.toString( now / 1000L);
        String nonce = Long.toHexString(now) + "-" + Long.toHexString((long) Math.floor(Math.random() * 0xFFFFFF));  //签名盐
        String signature = toSM3(String.format("%s%s%s%s",timestamp,nonce,appSecret,timestamp));
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        map.put("signature",signature);
        return map;
    }

    /**生成sm3算法hash
     * 2023/8/10-22:40
     * @author pengfulin
    */
    public static String toSM3(String str){
        SM3Digest sm3 = new SM3Digest();
        byte[] md = new byte[32];
        byte[] psw = str.getBytes(StandardCharsets.UTF_8);
        sm3.update(psw, 0, psw.length);
        sm3.doFinal(md, 0);
        return bytesToHex(md);
    }


    /**16进制转换
     * 2023/8/10-22:41
     * @author pengfulin
    */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                result.append("0");
            }
            result.append(temp);
        }
        return result.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

}
