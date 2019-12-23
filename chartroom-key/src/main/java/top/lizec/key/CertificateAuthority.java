package top.lizec.key;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CertificateAuthority {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH-mm-ss");

    /**
     * 创建CA的密钥对, 此操作在整个项目中只需要执行一次 创建完成后将文件移动到resources目录
     */
    private static void createCAKeyPair() throws Exception {
        KeyPair keyPair = createRSAKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        try (FileOutputStream out = new FileOutputStream("CA_RSA.pub")) {
            out.write(publicKey.getEncoded());
        }

        try (FileOutputStream out = new FileOutputStream("CA_RSA")) {
            out.write(privateKey.getEncoded());
        }
    }

    private static RSAPrivateKey loadPrivateKey() throws Exception {
        URI file = Objects.requireNonNull(CertificateAuthority.class.getClassLoader().getResource("CA_RSA")).toURI();
        byte[] buffer = Files.readAllBytes(Paths.get(file));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static RSAPublicKey loadPublicKey() throws Exception {
        URI file = Objects.requireNonNull(CertificateAuthority.class.getClassLoader().getResource("CA_RSA.pub")).toURI();
        byte[] buffer = Files.readAllBytes(Paths.get(file));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }


    /**
     * 创建证书文件和私钥并写入文件
     *
     * @param name 证明的名称
     */
    public static void createCertificate(String name) throws Exception {
        LocalDateTime expiration = LocalDateTime.now().plusMonths(3);
        KeyPair keyPair = createRSAKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        Certificate certificate = new Certificate(publicKey, name, expiration, loadPrivateKey());

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name + ".cert"))) {
            out.writeObject(certificate);
            out.flush();
        }

        try (FileOutputStream out = new FileOutputStream(name + ".private")) {
            out.write(privateKey.getEncoded());
        }
    }

    private static KeyPair createRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(4096, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        return keyPairGen.generateKeyPair();
//        // 得到私钥
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        // 得到公钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    public static void main(String[] args) throws Exception {
        //CertificateAuthority.createCAKeyPair();
//        LocalDateTime time = LocalDateTime.now().plusMonths(3);
//        System.out.println(time.format(formatter));
        //createCertificate("chartroom.lizec.top");
        //loadPrivateKey();

//        String file = CertificateAuthority.class.getClassLoader().getResource("CA_RAS").getFile();
//        System.out.println(file);
        //createCertificate("chartroom.lizec.top");


//        Certificate certificate = Certificate.loadFromURL(new File("chartroom.lizec.top.cert").toURL());
//        System.out.println(certificate.getName());
//        System.out.println(certificate.getDate());
//        System.out.println(certificate.toBASE65String());
//        System.out.println(certificate.checkSign(loadPublicKey()));
//
//        Certificate c2 = Certificate.loadFromBASE64(certificate.toBASE65String());
//        System.out.println(c2.getName());
//        System.out.println(c2.checkSign(loadPublicKey()));

    }


}
