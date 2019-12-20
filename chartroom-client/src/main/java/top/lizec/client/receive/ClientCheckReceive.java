package top.lizec.client.receive;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.User;

@ReceiveController("/check")
public class ClientCheckReceive {

    @GetMapping("/user")
    User getUserInfo() {
        // 服务器端调用此接口获得Socket的基本信息, 从而在后续管理
        return new User();
    }
}
