package top.lizec.core.security;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

import top.lizec.key.Certificate;
import top.lizec.key.CertificateAuthority;

public class CertManager {
    private String serverName;
    private RSAPrivateKey privateKey;
    private Certificate certificate;

    public CertManager(String serverName) {
        this.serverName = serverName;
        loadInfo();
    }

    private void loadInfo() {
        try {
            URL certFile = Objects.requireNonNull(this.getClass().getClassLoader().getResource(serverName + ".cert"));
            this.certificate = Certificate.loadFromURL(certFile);
            this.privateKey = loadPrivateKey();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | ClassNotFoundException | URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("证书信息加载失败");
        }
    }

    private RSAPrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, URISyntaxException {
        URI file = Objects.requireNonNull(CertificateAuthority.class.getClassLoader().getResource(serverName + ".private")).toURI();
        byte[] buffer = Files.readAllBytes(Paths.get(file));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public Certificate getCertificate() {
        return certificate;
    }
}
