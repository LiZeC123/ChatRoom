package top.lizec.server.controller;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;
import top.lizec.server.biz.MessageBiz;

import java.util.List;

@ReceiveController("/message")
public class MessageController {
    @Automatique
    private MessageBiz messageBiz;

    @GetMapping("/add")
    public String addMessage(Message message) {
        System.out.println("Server Receive Message:" + message);
        boolean isSuccess = messageBiz.forward(message);
        return isSuccess ? "Success" : "Fail";
    }

    @GetMapping("/getMessageByUser")
    public List<Message> getMessageByUser(Message message) {
        return messageBiz.getMessageFor(message);
    }

}
