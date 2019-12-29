package top.lizec.core.proxy;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;
import top.lizec.core.exception.MessageNotSendException;
import top.lizec.core.exception.UserLogoutException;
import top.lizec.core.security.CertManager;

public class PushSocketManager {
    private final List<ObjectSocket> sockets = new ArrayList<>();

    public void addSocket(Socket socket, String serverName, CertManager certManager) throws IOException {
        sockets.add(new ObjectSocket(socket, serverName, certManager));
    }

    void pushTo(LSTPEntityRequest request, String name) {
        Optional<ObjectSocket> var = sockets.stream().filter(objectSocket -> name.equals(objectSocket.getClientName()))
                .findFirst();

        var.ifPresent(socket -> {
            try {
                socket.writeUTF(request.toString());
                LSTPEntityResponse.parseFrom(socket.readUTF());
            } catch (UserLogoutException e) {
                sockets.remove(socket);
                System.out.println("用户" + name + "已经下线, 消息发送失败");
                System.out.println("Remove Socket For User: " + name);
                throw new UserLogoutException();
            } catch (IOException e) {
                sockets.remove(socket);
                System.err.println("向" + name + "发送消息失败,原因是: " + e.getMessage());
                System.out.println("Remove Socket For User: " + name);
                throw new MessageNotSendException();
            }
        });
        var.orElseThrow(MessageNotSendException::new);
    }
}

