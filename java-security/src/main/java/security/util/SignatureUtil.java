package security.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**常见签名算法工具
 * 2023/8/10-22:32
 * @author pengfulin
*/
public class SignatureUtil {

    /**签名生成:sha256算法
     * 2023/6/25 0025-11:57
     * @author pengfulin
     */
    public static Map<String,String> getSha256Signature(String token) throws Exception{
        long now = System.currentTimeMillis();
        String timestamp = Long.toString(now / 1000L);  //时间戳
        String nonce = Long.toHexString(now) + "-" + Long.toHexString((long) Math.floor(Math.random() * 0xFFFFFF));  //签名盐
        String signature = SignatureUtil.toSHA256(timestamp + token + nonce + timestamp);
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("signature",signature);
        map.put("nonce",nonce);
        return map;
    }

    /**生成sha256算法签名
     * 2023/8/10-22:39
     * @author pengfulin
    */
    public static String toSHA256(String str) throws Exception {
        MessageDigest messageDigest;
        String encodeStr = "";
        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        encodeStr = byte2Hex(messageDigest.digest());
        return encodeStr;
    }



    /**签名生成:sm3算法
     * 2023/6/25 0025-15:06
     * @author pengfulin
    */
    public static Map<String,String> getSM3Signature(String appKey,String requestId,String userToken,String appSecret) {
        String timestamp = Long.toString( System.currentTimeMillis() / 1000L);
        String signature = toSM3(String.format("%s%s%s%s%s", appKey,timestamp,requestId,userToken,appSecret));
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("signature",signature);
        map.put("appKey",appKey);
        return map;
    }

    /**生成sm3算法签名
     * 2023/8/10-22:40
     * @author pengfulin
    */
    public static String toSM3(String str){
        SM3Digest sm3 = new SM3Digest();
        byte[] md = new byte[32];
        byte[] psw = str.getBytes(StandardCharsets.UTF_8);
        sm3.update(psw, 0, psw.length);
        sm3.doFinal(md, 0);
        return byte2Hex(md);
    }


    /**16进制转换
     * 2023/8/10-22:41
     * @author pengfulin
    */
    private static String byte2Hex(byte[] bytes) {
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
}
