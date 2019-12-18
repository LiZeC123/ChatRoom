package top.lizec.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.PostMapping;
import top.lizec.core.annotation.RequestController;
import top.lizec.core.entity.User;

@RequestController("/user")
public class UserController {

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/signUp")
    public String signUp(User user) {
        try {
            //User user = mapper.readValue(jsonString,User.class);
            return "Token";
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: 把字符串转成Java对象, 然后处理
        // 如果不能转换, 则可以直接抛出异常
        // Get方法不接受参数, POST方法接受参数
        // Token包含在头部, 在业务代码层面不体现
        return null;
    }

    @GetMapping("/login")
    public String login() throws Exception {
        //throw new Exception();
        return "Hello World!";
    }

    @GetMapping("/online")
    public String isOnline() {
        return "Hello From Server!";
    }
}
