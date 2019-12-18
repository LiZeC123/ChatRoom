package top.lizec.server;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.LSTPServer;

@LSTPServer
@AutomatiqueScan("top.lizec.server")
public class ChartRoomServerApplication {
    // HTTP如何转换为HTTPS
    // 两个方向都做动态代理
    // 根据配置动态决定启用HTTP模式还是HTTPS模式


    // 从Controller层开始, 就可以按照Spring的模式分割业务逻辑
    // 但是没有必要搞这么复杂, 能不用数据库就不使用数据库
    // 可以使用文件保存数据
    public static void main(String[] args) {
        LZApplication.run(ChartRoomServerApplication.class);
    }
}
