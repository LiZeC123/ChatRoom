package top.lizec.core.proxy;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;
import top.lizec.core.exception.MessageNotSendException;
import top.lizec.core.exception.NameNotGetException;
import top.lizec.core.exception.UserLogoutException;
import top.lizec.core.security.CertManager;

public class PushSocketManager {
    private final List<ObjectSocket> sockets = new ArrayList<>();

    public void addSocket(Socket socket, String serverName, CertManager certManager) throws IOException {
        sockets.add(new ObjectSocket(socket, serverName, certManager));
    }

    void pushTo(LSTPEntityRequest request, String name) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(name);
        // 客户端无应答, 移除连接
        Iterator<ObjectSocket> it = sockets.iterator();
        while (it.hasNext()) {
            ObjectSocket socket = it.next();
            try {
                socket.getClientName();
            } catch (NameNotGetException e) {
                it.remove();
            }
        }

        List<ObjectSocket> nameList = sockets.stream()
                .filter(objectSocket -> name.equals(objectSocket.getClientName()))
                .collect(Collectors.toList());

        // 移除同名的连接, 只保留最后一个
        int length = nameList.size();
        for (int i = 0; i < length - 1; i++) {
            sockets.remove(nameList.get(i));
        }

        if (length > 0) {
            // 2. 获得名称，但是抛出异常， 移除并遍历下一个
            ObjectSocket thisSocket = nameList.get(length - 1);
            try {
                thisSocket.writeUTF(request.toString());
                LSTPEntityResponse.parseFrom(thisSocket.readUTF());
            } catch (UserLogoutException e) {
                sockets.remove(thisSocket);
                System.out.println("用户" + name + "已经下线, 消息发送失败");
                System.out.println("Remove Socket For User: " + name);
                throw new UserLogoutException();
            } catch (IOException e) {
                sockets.remove(thisSocket);
                System.err.println("向" + name + "发送消息失败,原因是: " + e.getMessage());
                System.out.println("Remove Socket For User: " + name);
                throw new MessageNotSendException();
            }
        } else {
            // 3. 如果所有都没有， 则抛出无法推送异常
            throw new MessageNotSendException();
        }
    }
}

