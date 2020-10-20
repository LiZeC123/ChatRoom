package top.lizec.client.request;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.RequestController;
import top.lizec.core.biz.Message;

import java.util.List;

@RequestController("/message")
public interface MessageRequester {

    @GetMapping("/add")
    String addMessage(Message message);

    @GetMapping("/getMessageByUser")
    List<Message> getMessageByUser(Message message);
}
