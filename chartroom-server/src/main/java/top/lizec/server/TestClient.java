package top.lizec.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import top.lizec.core.entity.LSTPEntityRequest;

public class TestClient {

    public static void main(String[] args) throws IOException {
        LSTPEntityRequest request = LSTPEntityRequest.createGetWith("/message/add", "{\"username\":\"GGBoy\",\"content\":\"Across the Great Wall, we can reach every corner in the world.\"}");
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
