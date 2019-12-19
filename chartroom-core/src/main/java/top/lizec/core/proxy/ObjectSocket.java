package top.lizec.core.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ObjectSocket {
    private ObjectOutputStream out;
    private ObjectInputStream in;

    ObjectSocket(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    void writeUTF(String string) throws IOException {
        out.writeUTF(string);
        out.flush();
    }

    String readUTF() throws IOException {
        return in.readUTF();
    }
}
