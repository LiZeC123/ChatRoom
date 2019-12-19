package top.lizec.client;

import top.lizec.client.request.UserRequester;
import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
class RequestTest {
    @Automatique
    private UserRequester userRequester;

    void doTest() {
        User user = new User();
        user.setUsername("LiZeC");
        user.setPassword("123456");
        userRequester.signUp(user);
    }
}
