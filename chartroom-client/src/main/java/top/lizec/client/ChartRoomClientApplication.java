package top.lizec.client;

import top.lizec.core.LZApplication;
import top.lizec.core.annotation.ReceiveScan;
import top.lizec.core.annotation.RequestScan;

@ReceiveScan("")
@RequestScan("") // 指定发送Request请求的包
public class ChartRoomClientApplication {
    public static void main(String[] args) {
        LZApplication.run(ChartRoomClientApplication.class);
    }
}
