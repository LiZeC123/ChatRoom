package top.lizec.server.controller;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.entity.User;

@ReceiveController("/user")
public class UserController {

    @GetMapping("/signUp")
    public User signUp(User user) {
        System.out.println(user);
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
