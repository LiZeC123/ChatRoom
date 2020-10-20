package top.lizec.core.security.entity;

import top.lizec.key.Certificate;

import java.io.IOException;

public class SecurityCertificateBody {
    private String certificateStr;
    private Long randomNum;


    public SecurityCertificateBody() {
    }

    public SecurityCertificateBody(Certificate certificate, Long randomNum) throws IOException {
        certificateStr = certificate.toBASE65String();
        this.randomNum = randomNum;
    }

    public String getCertificateStr() {
        return certificateStr;
    }

    public Long getRandomNum() {
        return randomNum;
    }
}
