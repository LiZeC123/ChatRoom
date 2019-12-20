package top.lizec.core.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import top.lizec.core.entity.LSTPEntityRequest;

class ObjectSocket {
    private static final String finishInfo = LSTPEntityRequest.createGetWith("__finish__", "Empty").toString();
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean closed = false;

    ObjectSocket(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    void writeUTF(String string) throws IOException {
        try {
            out.writeUTF(string);
            out.flush();
        } catch (IOException e) {
            closed = true;
        }

    }

    String readUTF() throws IOException {
        try {
            return in.readUTF();
        } catch (IOException e) {
            closed = true;
            return finishInfo;
        }
    }

    boolean hasClosed() {
        return closed;
    }
}
