package security.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public static Map<String,String> doGetCommonSignature(List<String> tokenData,String tokenTemplate,SignatureHash signatureHash) throws Exception{
        String token = String.format(tokenTemplate, tokenData);
        return doGetSimpleSignature(token,signatureHash);
    }

    /**
     * 生成签名：通用方式
     * 2024/4/2 0002 11:49
     * @author fulin-peng
     */
    public static Map<String,String> doGetSimpleSignature(String token,SignatureHash signatureHash) throws Exception{
        long now = System.currentTimeMillis();
        String timestamp = Long.toString( now / 1000L);
        String nonce = Long.toHexString(now) + "-"
                   /*
                *1.Math.random():Math.random()生成一个大于或等于0.0且小于1.0的双精度浮点数（double）。
                *2.Math.random() * 0xFFFFFF:这个表达式将Math.random()生成的随机数乘以0xFFFFFF（十六进制表示的16777215）。
                   结果是一个大于或等于0.0且小于16777216.0（即0xFFFFFF + 1）的浮点数。这个乘法操作使得你可以得到一个在指定范围内（0到0xFFFFFF）的随机浮点数。
                *3.(long) Math.floor(...):Math.floor(...)接受一个双精度浮点数（double），并返回小于或等于该数的最大整数。因为乘法结果是浮点数，使用Math.floor可以确保得到一个整数值。
                   将Math.floor(...)的结果强制转换为long类型，是为了确保结果能作为长整型值处理，这对于接下来转换为十六进制字符串是必要的。请注意，这里的强制类型转换通常是多余的，
                   因为Math.floor(...)的结果已经是long范围内的整数，但这样做确保了类型的明确性。
                *4. Long.toHexString(...):Long.toHexString(long i)是一个静态方法，接受一个长整型（long）参数，并返回该参数的十六进制字符串表示。在这个例子中，它将步骤3中得到的长整型数转换
                    为一个十六进制字符串。
                **/
                + Long.toHexString((long) Math.floor(Math.random() * 0xFFFFFF));  //签名盐
        String signature =null;
        String signatureSource=timestamp + token + nonce + timestamp;
        if(signatureHash==SignatureHash.SHA_256){
            signature=toSHA256(signatureSource);
        }else if(signatureHash==SignatureHash.SM3){
            signature=toSM3(signatureSource);
        }else if (signatureHash==SignatureHash.MD5){
            signature=toMd5(signatureSource);
        }else {
            return null;
        }
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        map.put("signature",signature);
        return map;
    }


    /**生成sha256算法hash
     * 2023/8/10-22:39
     * @author pengfulin
    */
    public static String toSHA256(String str) throws Exception {
        return toCommonAlgorithm(str,"SHA-256");
    }

    /**
     * 生成md5算法hash
     * 2024/1/9 0009 11:15
     * @author fulin-peng
     */
    public static String toMd5(String str) throws Exception {
        return toCommonAlgorithm(str,"MD5");
    }

    /**
     * 生成通用算法hash
     * 2024/1/9 0009 11:16
     * @author fulin-peng
     */
    public static String toCommonAlgorithm(String str,String algorithm) throws Exception {
        MessageDigest messageDigest;
        String encodeStr = "";
        messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        encodeStr = bytesToHex(messageDigest.digest());
        return encodeStr;
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
    public static String bytesToHex(byte[] bytes) {
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


    /**
     * 签名认证：通用方式
     * 2024/4/2 0002 11:28
     * @author fulin-peng
     */
    public static boolean validateSimpleSignature(Map<String,String> signatureData,String token,SignatureHash signatureHash) throws Exception{
        return validateSimpleSignature(signatureData,token,signatureHash,0);
    }

    /**
     * 签名认证：通用方式
     * 2024/4/2 0002 11:28
     * @author fulin-peng
     */
    public static boolean validateSimpleSignature(Map<String,String> signatureData,String token,SignatureHash signatureHash,int validTime) throws Exception{
        String timestamp = signatureData.get("timestamp");
        String nonce = signatureData.get("nonce");
        String signature = signatureData.get("signature");
        if(timestamp==null|| nonce==null||signature==null)
            return false;
        String signatureSource = timestamp + token + nonce + timestamp;
        boolean signResult=false;
        if(signatureHash==SignatureHash.SHA_256){
            signResult= signature.equals(toSHA256(signatureSource));
        } else if (signatureHash==SignatureHash.SM3) {
            signResult= signature.equals(toSM3(signatureSource));
        } else if(signatureHash==SignatureHash.MD5) {
            signResult= signature.equals(toMd5(signatureSource));
        }
        long timeDifference = (System.currentTimeMillis() / 1000L) - Long.parseLong(timestamp);
        return validTime<=0?signResult:signResult && timeDifference>=0 && timeDifference <=validTime;
    }
}
