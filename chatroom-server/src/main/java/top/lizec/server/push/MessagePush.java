package top.lizec.server.push;

import top.lizec.core.annotation.PushController;
import top.lizec.core.annotation.PushMapping;
import top.lizec.core.biz.Message;

import java.util.List;

@PushController
public interface MessagePush {
    @PushMapping("/push/message")
    void pushTo(Message message, String name);

    @PushMapping("/push/message")
    void pushTo(Message message, List<String> names);
}
