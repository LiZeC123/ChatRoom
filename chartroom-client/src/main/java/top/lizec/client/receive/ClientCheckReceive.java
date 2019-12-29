package top.lizec.client.receive;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.proxy.Context;

@ReceiveController("/check")
public class ClientCheckReceive {
    @Automatique
    Context context;

    @GetMapping("/user")
    public String getUserInfo() {
        System.out.println("Receive Name Query, name is " + context.getValueByName("name"));
        return context.getValueByName("name");
    }

    @GetMapping("/isOnline")
    public String isOnline() {
        return "I am still alive!";
    }
}
