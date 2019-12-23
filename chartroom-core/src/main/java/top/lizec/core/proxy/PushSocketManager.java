package top.lizec.core.proxy;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;
import top.lizec.core.security.CertManager;

public class PushSocketManager {
    private final List<ObjectSocket> sockets = new ArrayList<>();

    public void addSocket(Socket socket, String serverName, CertManager certManager) throws IOException {
        sockets.add(new ObjectSocket(socket, serverName, certManager));
    }

    void pushMessage(LSTPEntityRequest request) {
        String strRequest = request.toString();
        for (ObjectSocket socket : sockets) {
            synchronized (sockets) {
                try {
                    socket.writeUTF(strRequest);
                    LSTPEntityResponse.parseFrom(socket.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

