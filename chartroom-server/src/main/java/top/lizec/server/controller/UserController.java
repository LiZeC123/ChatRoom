package top.lizec.server.controller;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.User;
import top.lizec.server.biz.UserBiz;

@ReceiveController("/user")
public class UserController {
    @Automatique
    private UserBiz userBiz;

    @GetMapping("/signUp")
    public User signUp(User user) {
        return userBiz.signUp(user);
    }

    @GetMapping("/login")
    public User login(User user) {
        return userBiz.login(user);
    }

    @GetMapping("/online")
    public String isOnline() {
        return "Hello From Server!";
    }
}
