package top.lizec.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChartRoomServer {
    private ServerSocket serverSocket;

    public ChartRoomServer() throws IOException {
        serverSocket = new ServerSocket(8848);
    }

    public void accept() throws IOException {
        Socket socket = serverSocket.accept();

    }
}
