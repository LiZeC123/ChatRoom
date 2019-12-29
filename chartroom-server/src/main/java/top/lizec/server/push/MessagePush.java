package top.lizec.server.push;

import java.util.List;

import top.lizec.core.annotation.PushController;
import top.lizec.core.annotation.PushMapping;
import top.lizec.core.biz.Message;

@PushController
public interface MessagePush {
    @PushMapping("/push/message")
    void pushTo(Message message, String name);

    @PushMapping("/push/message")
    void pushTo(Message message, List<String> names);
}
