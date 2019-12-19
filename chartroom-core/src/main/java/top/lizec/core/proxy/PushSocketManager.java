package top.lizec.core.proxy;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;

public class PushSocketManager {
    private final List<BasicObjectSocket> sockets = new ArrayList<>();

    public void addSocket(Socket socket) throws IOException {
        sockets.add(new BasicObjectSocket(socket));
    }

    void pushMessage(LSTPEntityRequest request) {
        String strRequest = request.toString();
        for (BasicObjectSocket socket : sockets) {
            synchronized (sockets) {
                try {
                    socket.getOut().writeUTF(strRequest);
                    socket.getOut().flush();
                    LSTPEntityResponse.parseFrom(socket.getIn().readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

