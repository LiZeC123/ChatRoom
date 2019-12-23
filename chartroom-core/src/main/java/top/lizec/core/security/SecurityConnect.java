package top.lizec.core.security;

import java.net.Socket;

public class SecurityConnect {
    private Socket socket;
    private boolean isServer;


    // 需要确定 这个类和ObjectSocket的调用关系, 到底是谁调用谁
    // 因为在OS的构造函数中执行, 因此可以保证不会被拦截

    public SecurityConnect(Socket socket, boolean isServer) {
        this.socket = socket;
        this.isServer = isServer;
    }


}
