package top.lizec.server;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.LSTPServer;

@LSTPServer(name = "chartroom.lizec.top")
@AutomatiqueScan("top.lizec.server")
public class ChartRoomServerApplication {
    public static void main(String[] args) {
        LZApplication.run(ChartRoomServerApplication.class);
    }
}
