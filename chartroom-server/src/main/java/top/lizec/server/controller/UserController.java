package top.lizec.server.controller;

import java.util.List;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.biz.User;
import top.lizec.server.biz.UserBiz;

@ReceiveController("/user")
public class UserController {
    @Automatique
    private UserBiz userBiz;

    /**
     * 注册用户, 并返回Token
     */
    @GetMapping("/signUp")
    public String signUp(User user) {
        return userBiz.signUp(user);
    }

    /**
     * 登录用户, 并返回Token
     */
    @GetMapping("/login")
    public String login(User user) {
        return userBiz.login(user);
    }

    /**
     * 注销用户和相应的Token
     */
    @GetMapping("/logout")
    public String logout(User user) {
        return userBiz.logout(user);
    }


    @GetMapping("/friendList")
    public List<String> friendList(User user) {
        return userBiz.friendList(user);
    }

    @GetMapping("/online")
    public String isOnline() {
        return "Hello From Server!";
    }
}
