package top.lizec.client.receive;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;

@ReceiveController("/push")
public class PushMessageReceive {

    @GetMapping("/message")
    public void receiveMessage(Message message) {
        System.out.println("Client Receive Message:" + message);
    }
}
