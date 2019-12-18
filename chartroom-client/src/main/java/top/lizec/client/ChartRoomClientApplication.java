package top.lizec.client;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.AutomatiqueScan;

@AutomatiqueScan("top.lizec.client")
public class ChartRoomClientApplication {

    /*
     * 1. 客户端如何构成?
     * 2. 那个线程是主线程
     * 发送的消息是固定的内容, 响应还是使用注解
     * 所有的请求必须共享一个Socket, 从而响应也使用一个Socket
     * 主动请求根据类使用不同的Socket, 所有的响应在启动时创建并共享一个Socket
     *
     *
     * */
    public static void main(String[] args) {
        LZApplication.run(ChartRoomClientApplication.class);
    }
}
