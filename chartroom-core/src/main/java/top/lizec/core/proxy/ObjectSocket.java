package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.security.CertManager;
import top.lizec.core.security.KeyCalculator;
import top.lizec.core.security.entity.SecurityCertificateBody;
import top.lizec.core.security.entity.SecurityRequest1Body;
import top.lizec.key.Certificate;
import top.lizec.key.CertificateAuthority;

class ObjectSocket {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String finishInfo = LSTPEntityRequest.createGetWith("__finish__", "Empty").toString();
    private static final KeyCalculator keyCalculator = new KeyCalculator();

    private static final boolean hasSecurity = false; // 是否启动加密

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean closed = false;

    private String serverName;   // 服务器端名称
    private CertManager certManager;

    // 加密通信设计
    // 1. 在底层封装, 对高层无感知
    //
    /*
     * 通信过程
     * 1. 客户端向服务器端发送请求和随机数1
     * 2. 服务器端接受请求后, 向客户端发送证书/ 公钥文件 + 随机数2
     * 3. 客户端验证证书, 使用公钥加密随机数3
     * 4. 客户端和服务器端根据三个随机数各自计算对称密钥
     *
     *
     * */


    ObjectSocket(Socket socket, String serverName, CertManager certManager) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.serverName = serverName;
        this.certManager = certManager;
        if (hasSecurity) {
            connectWithSecurity();
        }
    }

    ObjectSocket(Socket socket) throws IOException {
        this(socket, null, null);
    }

//    ObjectSocket(Socket socket) throws IOException {
//        this(socket,null);
//    }

    private boolean isServer() {
        return serverName != null;
    }


    private void connectWithSecurity() throws IOException {
        if (isServer()) {
            try {
                doServerShake();
            } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                doClientShake();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    void writeUTF(String string) throws IOException {
        if (hasSecurity) {
            _writeUTF(keyCalculator.encodeWithRandomKey(string));
        } else {
            _writeUTF(string);
        }
    }

    String readUTF() throws IOException {
        if (hasSecurity) {
            return keyCalculator.decodeWithRandomKey(_readUTF());
        } else {
            return _readUTF();
        }
    }

    private void _writeUTF(String content) {
        try {
            out.writeUTF(content);
            out.flush();
        } catch (IOException e) {
            closed = true;
            closeStream();
        }
    }

    private String _readUTF() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            closed = true;
            closeStream();
            return finishInfo;
        }
    }

    boolean hasClosed() {
        return closed;
    }

    private void closeStream() {
        try {
            in.close();
        } catch (IOException ignored) {

        }
        try {
            out.close();
        } catch (IOException ignored) {

        }
    }

    private void doClientShake() throws Exception {
        // 1. 发送可用算法与随机数1
        Long random1 = keyCalculator.getRandomLong();
        SecurityRequest1Body securityRequestBody = new SecurityRequest1Body("RSA", random1);
        LSTPEntityRequest request = LSTPEntityRequest
                .createGetWith("__Security:SendRequest", mapper.writeValueAsString(securityRequestBody));
        _writeUTF(request.toString());

        // 2. 接受证书和随机数2
        LSTPEntityRequest response = LSTPEntityRequest.parseFrom(_readUTF());
        SecurityCertificateBody body = mapper.readValue(response.getBody(), SecurityCertificateBody.class);
        Long random2 = body.getRandomNum();
        Certificate certificate = Certificate.loadFromBASE64(body.getCertificateStr());
        boolean checkResult = certificate.checkSign(CertificateAuthority.loadPublicKey());
        if (!checkResult) {
            throw new Exception("数字签名验证错误");
        }

        // 3. 使用服务器公钥加密随机数3
        Long random3 = keyCalculator.getRandomLong();
        String cipherData = keyCalculator.encodeWithKey(random3, certificate.getPublicKey());
        //SecurityRequest3Body body3 = new SecurityRequest3Body(cipherData);
        LSTPEntityRequest requestNum = LSTPEntityRequest.createGetWith("__Security:SendNum", mapper.writeValueAsString(cipherData));
        _writeUTF(requestNum.toString());

        // 4. 计算对称密钥
        calcKey(random1, random2, random3);
    }

    private void doServerShake() throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        // 1. 接受客户端请求
        LSTPEntityRequest request1 = LSTPEntityRequest.parseFrom(_readUTF());
        SecurityRequest1Body body = mapper.readValue(request1.getBody(), SecurityRequest1Body.class);
        if (body.getAlgorithm().equals("none")) {
            // ...
            return;
        }
        Long random1 = body.getRandomNum();

        // 2. 发送证书和随机数2
        Long random2 = keyCalculator.getRandomLong();
        Certificate certificate = certManager.getCertificate();
        SecurityCertificateBody securityCertificateBody = new SecurityCertificateBody(certificate, random2);
        LSTPEntityRequest request2 = LSTPEntityRequest.createGetWith("__Security:certificate", mapper.writeValueAsString(securityCertificateBody));
        _writeUTF(request2.toString());

        // 3. 接收随机数3
        LSTPEntityRequest request3 = LSTPEntityRequest.parseFrom(_readUTF());
        String num = mapper.readValue(request3.getBody(), String.class);
        Long random3 = keyCalculator.decodeWithKey(num, certManager.getPrivateKey());

        // 4. 计算对称密钥
        calcKey(random1, random2, random3);
    }

    private void calcKey(Long random1, Long random2, Long random3) {
        Long randomKey = random1 ^ random2 ^ random3;
        System.out.println("Random Key is " + randomKey);
        keyCalculator.initByte(randomKey);
    }
}
