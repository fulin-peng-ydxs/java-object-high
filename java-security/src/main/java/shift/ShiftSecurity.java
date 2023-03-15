package shift;




/**
 * @author PengFuLin
 * @version 1.0
 * @description: 移位加密演示
 * @date 2021/10/21 21:32
 */
public class ShiftSecurity {

    public static void main(String[] args) {
        int key = 500;
        String result=null;
        System.out.println("加密：");
        System.out.println(result=encrypt("梁育辉", key));
        System.out.println("解密：");
        System.out.println(decrypt(result,key));
    }


    /**
     * @Author PengFuLin
     * @Description 加密
     * @Date 21:57 2021/10/21
     * @Param [data:原文, key：加密密钥]
     * @return 密文
     **/
    private static String encrypt(String data,int key){
//        byte[] bytes = data.getBytes();
//        byte[] resultByte = new byte[bytes.length];
//        for (int i = 0; i < bytes.length; i++) {
//            resultByte[i]=(byte) ((byte) bytes[i]+key);
//        }
//        return new String(resultByte);
        char[] chars = data.toCharArray();
        StringBuilder buffer = new StringBuilder();
        for (char aChar : chars) {
            buffer.append((char) (((int)aChar)+key));
        }
        return buffer.toString();
    }


    /**
     * @Author PengFuLin
     * @Description  解密
     * @Date 22:24 2021/10/21
     * @Param [data：密文, key：密钥]
     * @return 原文
     **/
    private static  String decrypt(String data,int key){
        char[] chars = data.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char aChar : chars) {
            builder.append((char) (((int)aChar)-key));
        }
        return builder.toString();
    }
}
