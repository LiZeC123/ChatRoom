package top.lizec.test.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.lizec.core.biz.Message;
import top.lizec.core.entity.LSTPEntityRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        Message message = new Message("ggboy", "3fs", "Across the Great Wall, we can reach every corner in the world.", "lizec");

        LSTPEntityRequest request = LSTPEntityRequest.createGetWith("/message/add", mapper.writeValueAsString(message));
        Socket socket = new Socket("localhost", 8848);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        String pack = request.toString();
        System.out.println(pack);
        out.writeUTF(pack);
        out.flush();

        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        String response = in.readUTF();
        System.out.println(response);

        in.close();
        out.close();
    }
}
