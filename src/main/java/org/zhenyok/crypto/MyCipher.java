package org.zhenyok.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class MyCipher {
    private static final byte[] keyBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private static final String algorithm = "AES";
    private static final SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);


    public static byte[] encrypt(final byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(message);
    }

    public static byte[] decrypt(final byte[] message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(message);
    }
}
