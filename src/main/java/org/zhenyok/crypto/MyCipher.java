package org.zhenyok.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyCipher {
    private static final byte[] keyBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final byte[] ivBytes = new byte[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private static final String algorithm = "AES/CBC/PKCS5Padding";
    private static final SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    private static final IvParameterSpec iv = new IvParameterSpec(ivBytes);

    public static byte[] encrypt(final byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(message);
    }

    public static byte[] decrypt(final byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(message);
    }
}
