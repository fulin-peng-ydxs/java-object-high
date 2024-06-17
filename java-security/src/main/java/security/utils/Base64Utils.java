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
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

}
