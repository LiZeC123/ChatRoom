package top.lizec.server.biz;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;
import top.lizec.server.dao.TokenDao;
import top.lizec.server.dao.UserDao;

@Component
public class UserBiz {

    @Automatique
    private UserDao userDao;
    @Automatique
    private TokenDao tokenDao;

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

}
