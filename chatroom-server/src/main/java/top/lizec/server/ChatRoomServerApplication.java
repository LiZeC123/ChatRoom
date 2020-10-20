package top.lizec.server;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.LSTPServer;

@LSTPServer(name = "chatroom.lizec.top")
@AutomatiqueScan("top.lizec.server")
public class ChatRoomServerApplication {
    public static void main(String[] args) {
        LZApplication.run(ChatRoomServerApplication.class);
    }
}
