package top.lizec.server;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.ReceiveScan;

@ReceiveScan("top.lizec.server.controller")
public class ChartRoomServerApplication {
    // 从Controller层开始, 就可以按照Spring的模式分割业务逻辑
    // 但是没有必要搞这么复杂, 能不用数据库就不使用数据库
    // 可以使用文件保存数据
    public static void main(String[] args) {
        LZApplication.run(ChartRoomServerApplication.class);
    }
}
