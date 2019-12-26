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

    /**
     * 注册
     *
     * @param user 用户信息
     * @return 用户的Token
     */
    @GetMapping("/signUp")
    public String signUp(User user) {
        return userBiz.signUp(user);
    }

    /**
     * 登录
     *
     * @param user 用户信息
     * @return 用户的Token
     */
    @GetMapping("/login")
    public String login(User user) {
        return userBiz.login(user);
    }

    @GetMapping("/online")
    public String isOnline() {
        return "Hello From Server!";
    }
}
