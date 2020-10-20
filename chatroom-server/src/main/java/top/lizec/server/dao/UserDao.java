package top.lizec.server.dao;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

import java.util.List;

@Component
public class UserDao extends BaseDao<User> {


    public static void main(String[] args) {
        UserDao dao = new UserDao();
        dao.insert(new User("lizec", "1"));
        dao.insert(new User("ggboy", "1"));
        dao.getAllUser().forEach(System.out::println);
    }

    public User findUserByName(String username) {
        return super.findDateBy(u -> u.getUsername().equals(username));
    }

    public List<User> getAllUser() {
        return mList;
    }
}
