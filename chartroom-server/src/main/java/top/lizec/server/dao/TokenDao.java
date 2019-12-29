package top.lizec.server.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
public class TokenDao {
    private Map<String, String> userTokens = new HashMap<>();
    private Map<String, String> tokenUsers = new HashMap<>();

    public String getTokenForUser(User user) {
        if (!userTokens.containsKey(user.getUsername())) {
            String token = UUID.randomUUID().toString();
            userTokens.put(user.getUsername(), token);
            tokenUsers.put(token, user.getUsername());
        }
        return userTokens.get(user.getUsername());
    }

    public void cleanTokenForUser(User user) {
        String token = userTokens.remove(user.getUsername());
        if (token != null) {
            tokenUsers.remove(token);
        }
    }

    public boolean checkUserToken(User user) {
        if (user == null || user.getToken() == null) {
            return false;
        }
        if (!userTokens.containsKey(user.getUsername()) || !tokenUsers.containsKey(user.getToken())) {
            return false;
        }

        return userTokens.get(user.getUsername()).equals(user.getToken()) && tokenUsers.get(user.getToken()).equals(user.getUsername());
    }

}
