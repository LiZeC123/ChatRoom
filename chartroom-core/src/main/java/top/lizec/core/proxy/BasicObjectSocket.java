package top.lizec.core.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BasicObjectSocket implements ObjectSocket {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    BasicObjectSocket(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    @Override
    public void writeUTF(String string) throws IOException {
        out.writeUTF(string);
        out.flush();
    }

    @Override
    public String readUTF() throws IOException {
        return in.readUTF();
    }
}
