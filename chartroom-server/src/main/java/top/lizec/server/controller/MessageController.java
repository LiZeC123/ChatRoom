package top.lizec.server.controller;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;
import top.lizec.server.push.MessagePush;

@ReceiveController("/message")
public class MessageController {
    // 自动注入MessagePush类, 从而可以在受到消息后群发
    @Automatique
    private MessagePush messagePush;

    @GetMapping("/add")
    public String addMessage(Message message) {
        System.out.println("Server Receive Message:" + message);
        messagePush.pushMessage(message);
        return "Success";
    }

}
