package security.algorithm.hash;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

public class SM3Security {

    public static void main(String[] args) {
        String input = "Hello, SM3Security!";

        byte[] result = calculateSM3Hash(input.getBytes());
        String hashHex = Hex.toHexString(result);
        System.out.println("SM3Security Hash: " + hashHex);
    }

    public static byte[] calculateSM3Hash(byte[] input) {
        SM3Digest digest = new SM3Digest();
        digest.update(input, 0, input.length);

        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);

        return output;
    }
}
