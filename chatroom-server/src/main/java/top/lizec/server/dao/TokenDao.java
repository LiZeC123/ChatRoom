package top.lizec.server.dao;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenDao {
    private final Map<String, String> userTokens = new HashMap<>();
    private final Map<String, String> tokenUsers = new HashMap<>();

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

    public boolean checkUserToken(String username, String token) {
        if (username == null || token == null) {
            return false;
        }

        return token.equals(userTokens.get(username)) && username.equals(tokenUsers.get(token));
    }

}
