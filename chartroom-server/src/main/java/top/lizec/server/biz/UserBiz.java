package top.lizec.server.biz;

import java.util.ArrayList;
import java.util.List;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.FriendAndMessage;
import top.lizec.core.biz.User;
import top.lizec.server.dao.FriendDao;
import top.lizec.server.dao.MessageDao;
import top.lizec.server.dao.TokenDao;
import top.lizec.server.dao.UserDao;
import top.lizec.server.entity.MessageR;

@Component
public class UserBiz {
    @Automatique
    private UserDao userDao;
    @Automatique
    private TokenDao tokenDao;
    @Automatique
    private FriendDao friendDao;
    @Automatique
    private MessageDao messageDao;

    public String signUp(User user) {
        User dataUser = userDao.findUserByName(user.getUsername());
        if (dataUser == null) {
            userDao.insert(user);
            tokenDao.getTokenForUser(user);
        }
        return null;
    }

    public String login(User user) {
        User dataUser = userDao.findUserByName(user.getUsername());
        if (dataUser != null) {
            return tokenDao.getTokenForUser(user);
        }
        return null;
    }

    public String logout(User user) {
        User dataUser = userDao.findUserByName(user.getUsername());
        if (dataUser != null) {
            tokenDao.cleanTokenForUser(user);
            return "Success";
        }
        return null;
    }

    public List<FriendAndMessage> friendList(User user) {
        if (tokenDao.checkUserToken(user)) {
            List<FriendAndMessage> messages = new ArrayList<>();
            List<String> friendNames = friendDao.findFriendByName(user.getUsername());
            for (String friendName : friendNames) {
                MessageR messageR = messageDao.getRecentMessageOf(user.getUsername(), friendName);
                if (messageR != null) {
                    messages.add(new FriendAndMessage(friendName, messageR.getContent()));
                } else {
                    messages.add(new FriendAndMessage(friendName, ""));
                }
            }
            return messages;
        }

        System.err.println("Token 验证失败, 无法请求此接口");
        return null;
    }
}
