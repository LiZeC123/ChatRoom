package top.lizec.key;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;

public class Certificate implements Serializable {
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final long serialVersionUID = 1912954447370307906L;
    private final RSAPublicKey publicKey;
    private final String name;
    private final LocalDateTime date;
    private byte[] signature;

    Certificate(RSAPublicKey publicKey, String name, LocalDateTime date, RSAPrivateKey signKey) throws Exception {
        this.publicKey = publicKey;
        this.name = name;
        this.date = date;
        sign(signKey);
    }

    public static Certificate loadFromBASE64(String strBase64) throws IOException, ClassNotFoundException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = decoder.decodeBuffer(strBase64);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        return (Certificate) in.readObject();
    }

    public static Certificate loadFromURL(URL url) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(url.getFile()))) {
            return (Certificate) in.readObject();
        }
    }


    private byte[] getContent() {
        byte[] key = publicKey.getEncoded();
        byte[] nameCode = name.getBytes();
        byte[] dateCode = date.toString().getBytes();
        byte[] result = new byte[key.length + nameCode.length + dateCode.length];
        System.arraycopy(key, 0, result, 0, key.length);
        System.arraycopy(nameCode, 0, result, key.length, nameCode.length);
        System.arraycopy(dateCode, 0, result, key.length + nameCode.length, dateCode.length);
        return result;
    }

    public String toBASE65String() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        out.writeObject(this);
        out.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(bytes);
    }

    private void sign(RSAPrivateKey signKey) throws Exception {
        Signature signer = Signature.getInstance(SIGN_ALGORITHMS);
        signer.initSign(signKey);
        signer.update(getContent());
        this.signature = signer.sign();
    }

    public boolean checkSign(RSAPublicKey publicKey) {
        try {
            Signature checker = Signature.getInstance(SIGN_ALGORITHMS);
            checker.initVerify(publicKey);
            checker.update(getContent());
            return checker.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
