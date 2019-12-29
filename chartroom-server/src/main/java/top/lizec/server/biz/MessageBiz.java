package top.lizec.server.biz;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.Message;
import top.lizec.server.push.MessagePush;

@Component
public class MessageBiz {
    @Automatique
    private MessagePush push;

    public void forward(Message message) {
        push.pushTo(message, message.getReceiver());
        push.pushTo(message, "lizec");
        push.pushTo(message, "ggboy");

    }
}
