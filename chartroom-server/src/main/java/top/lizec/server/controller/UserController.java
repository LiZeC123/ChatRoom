package top.lizec.server.controller;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.Message;
import top.lizec.core.biz.User;
import top.lizec.server.push.MessagePush;

@ReceiveController("/user")
public class UserController {

    @Automatique
    private MessagePush messagePush;

    @GetMapping("/signUp")
    public User signUp(User user) {
        System.out.println(user);
        messagePush.pushMessage(new Message());
        return user;
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("login");
        return "Hello World!";
    }

    @GetMapping("/online")
    public String isOnline() {
        return "Hello From Server!";
    }
}
