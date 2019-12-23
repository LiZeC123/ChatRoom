package top.lizec.core.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class KeyCalculator {
    private BASE64Encoder encoder = new BASE64Encoder();
    private BASE64Decoder decoder = new BASE64Decoder();
    private Random random = new Random();
    private byte[] encodeByte = new byte[256];

    public void initByte(Long randomKey) {
        Random r = new Random();
        r.setSeed(randomKey);
        r.nextBytes(encodeByte);
    }

    public String encodeWithRandomKey(String content) {
        byte[] contents = content.getBytes();
        int p = 0;
        for (int i = 0; i < contents.length; i++) {
            contents[i] = (byte) (contents[i] ^ encodeByte[p++]);
            if (p >= encodeByte.length) {
                p = 0;
            }
        }
        return encoder.encode(contents);
    }

    public String decodeWithRandomKey(String content) throws IOException {
        byte[] contents = decoder.decodeBuffer(content);
        int p = 0;
        for (int i = 0; i < contents.length; i++) {
            contents[i] = (byte) (contents[i] ^ encodeByte[p++]);
            if (p >= encodeByte.length) {
                p = 0;
            }
        }
        return new String(contents);
    }

    public long getRandomLong() {
        return random.nextLong();
    }

    public String encodeWithKey(Long num, RSAPublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(cipher.doFinal(num.toString().getBytes()));
    }

    public Long decodeWithKey(String cipherData, RSAPrivateKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = cipher.doFinal(decoder.decodeBuffer(cipherData));
        return Long.valueOf(new String(bytes));
    }

}
