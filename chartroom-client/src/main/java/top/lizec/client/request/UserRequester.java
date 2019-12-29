package top.lizec.client.request;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.RequestController;
import top.lizec.core.biz.User;

@RequestController("/user")
public interface UserRequester {

    @GetMapping("/signUp")
    String signUp(User user);

    @GetMapping("/login")
    String login(User user);
}
