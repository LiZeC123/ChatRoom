package top.lizec.server.push;

import top.lizec.core.annotation.PushController;
import top.lizec.core.annotation.PushMapping;
import top.lizec.core.biz.Message;

@PushController
public interface MessagePush {

    @PushMapping("/push/message")
    void pushMessage(Message message);
}
