package top.lizec.client.receive;

import javafx.application.Platform;
import top.lizec.client.gui.MainController;
import top.lizec.client.message.MessagePool;
import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;
import top.lizec.core.proxy.Context;

@ReceiveController("/push")
public class PushMessageReceive {
    @Automatique
    Context context;

    @Automatique
    MessagePool messagePool;

    @GetMapping("/message")
    public void receiveMessage(Message message) {
        System.out.println("On Recv" + Thread.currentThread().getName());
        System.out.println("Client Receive Message:" + message);
        if (message.getUsername().equals(context.getValueByName("name"))) {
            // 自己的消息需要翻转一下对象
            messagePool.addMessage(message.getReceiver(), message, true);
        } else {
            messagePool.addMessage(message.getUsername(), message, false);
        }


        //更新JavaFX的主线程的代码放在此处
        Platform.runLater(this::update);
    }

    private void update() {
        MainController mainController = context.getObjectByName("MainController");
        mainController.update();
    }

}
