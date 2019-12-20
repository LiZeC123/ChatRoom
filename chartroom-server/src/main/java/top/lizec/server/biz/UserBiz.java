package top.lizec.server.biz;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;
import top.lizec.server.dao.UserDao;

@Component
public class UserBiz {

    @Automatique
    private UserDao userDao;

    public User signUp(User user) {
        if (userDao.hasUser(user)) {
            return new User(user.getUsername(), "Session");
        } else {
            return user;
        }
    }

    public User login(User user) {
        return null;
    }

    public User logout(User user) {
        return null;
    }

}
