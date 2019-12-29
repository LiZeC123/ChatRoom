package top.lizec.server.controller;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;
import top.lizec.server.biz.MessageBiz;

@ReceiveController("/message")
public class MessageController {
    @Automatique
    private MessageBiz messageBiz;

    @GetMapping("/add")
    public String addMessage(Message message) {
        System.out.println("Server Receive Message:" + message);
        messageBiz.forward(message);
        return "Success";
    }

}
