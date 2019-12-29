package top.lizec.test.tool;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.Socket;

import top.lizec.core.biz.User;
import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;
import top.lizec.core.proxy.ObjectSocket;

public class FriendClient {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        User user = new User("lizec", "1");

        LSTPEntityRequest request = LSTPEntityRequest.createGetWith("/user/login", mapper.writeValueAsString(user));
        ObjectSocket socket = new ObjectSocket(new Socket("chartroom.lizec.top", 8848));
        socket.writeUTF(request.toString());
        LSTPEntityResponse response = LSTPEntityResponse.parseFrom(socket.readUTF());

        System.out.println(response);
        user.setToken(mapper.readValue(response.getBody(), String.class));
        request = LSTPEntityRequest.createGetWith("/user/friendList", mapper.writeValueAsString(user));
        socket.writeUTF(request.toString());
        response = LSTPEntityResponse.parseFrom(socket.readUTF());

        System.out.println(response);
    }
}
