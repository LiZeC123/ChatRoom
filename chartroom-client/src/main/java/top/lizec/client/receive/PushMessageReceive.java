package top.lizec.client.receive;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;

@ReceiveController("/push")
public class PushMessageReceive {

    //@Automatique
    //MainWindow mainWindow;

    @GetMapping("/message")
    public void receiveMessage(Message message) {
        System.out.println("On Recv" + Thread.currentThread().getName());
        //mainWindow.setContent("<" + message.getUsername() + ">:" + message.getContent());
        System.out.println("Client Receive Message:" + message);
    }
}
