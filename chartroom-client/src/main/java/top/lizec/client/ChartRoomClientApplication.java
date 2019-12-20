package top.lizec.client;

import javax.swing.*;

import top.lizec.client.gui.MainWindow;
import top.lizec.core.LZApplication;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.LSTPClient;
import top.lizec.core.proxy.Context;
import top.lizec.core.proxy.UserThread;

@LSTPClient
@AutomatiqueScan("top.lizec.client")
public class ChartRoomClientApplication {

    /*
     * 1. 客户端如何构成?
     * 2. 那个线程是主线程
     * 发送的消息是固定的内容, 响应还是使用注解
     * 所有的请求必须共享一个Socket, 从而响应也使用一个Socket
     * 主动请求根据类使用不同的Socket, 所有的响应在启动时创建并共享一个Socket
     * 客户端在启动后创建一个Socket连接到服务端, 用于接收推送消息
     * 服务端在启动后创建监听Socket接收客户端发送的请求
     * */
    public static void main(String[] args) {
        LZApplication.run(ChartRoomClientApplication.class, new UserThread() {
            public void run(Context context) {
                MainWindow mainWindow = context.getObjectByType(MainWindow.class);
                JFrame jFrame = new JFrame("Test");
                jFrame.setContentPane(mainWindow.getMainPanel());
                jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jFrame.pack();
                jFrame.setVisible(true);
//
//
//                Random random = new Random();
//
//                MessageRequester requester = context.getObjectByType(MessageRequester.class);
//                for(int i=0;i<100;i++){
//                    Message message = new Message();
//                    message.setUsername("LiZeC");
//                    message.setContent("Text<"+i+">: This is a message!");
//                    try {
//                        Thread.sleep((long) (2000*random.nextFloat()));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    requester.addMessage(message);
//                }
            }
        });
    }
}
