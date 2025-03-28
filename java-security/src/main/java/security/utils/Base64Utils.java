package security.utils;

import java.util.Base64;

public class Base64Utils {


    /**
     * 编码
     * 2024/6/17 0017 16:50
     * @author fulin-peng
     */
    public static String encode(byte[] bytesToEncode){
        return Base64.getEncoder().encodeToString(bytesToEncode);
    }


    /**
     * 解码
     * 2024/6/17 0017 16:50
     * @author fulin-peng
     */
    public static String decode(String encodedString){
        return new String(decodeData(encodedString));
    }

    /**
     * 解码-原始数据
     * 2024/6/17 0017 16:50
     * @author fulin-peng
     */
    public static byte[] decodeData(String encodedString){
        return Base64.getDecoder().decode(encodedString);
    }

    /**
     * 判断是否是base64编码
     * 2025/3/28 10:38
     * @author pengshuaifeng
     * @param str
     */
    public static byte[] isBase64(String str) {
        try {
            return  decodeData(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
