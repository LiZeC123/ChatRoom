package top.lizec.server.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
public class TokenDao {
    private Map<User, String> userTokens = new HashMap<>();
    private Map<String, User> tokenUsers = new HashMap<>();

    public String getTokenForUser(User user) {
        if (!userTokens.containsKey(user)) {
            String token = UUID.randomUUID().toString();
            userTokens.put(user, token);
            tokenUsers.put(token, user);
        }
        return userTokens.get(user);
    }

    public void cleanTokenForUser(User user) {
        String token = userTokens.remove(user);
        if (token != null) {
            tokenUsers.remove(token);
        }
    }

    public boolean checkUserToken(User user) {
        if (user == null || user.getToken() == null) {
            return false;
        }
        if (!userTokens.containsKey(user) || !tokenUsers.containsKey(user.getToken())) {
            return false;
        }

        return userTokens.get(user).equals(user.getToken()) && tokenUsers.get(user.getToken()).equals(user);
    }

    public User getUserByToken(String token) {
        return tokenUsers.get(token);
    }
}
