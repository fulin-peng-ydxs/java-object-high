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

}
