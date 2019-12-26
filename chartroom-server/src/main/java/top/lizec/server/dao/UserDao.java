package top.lizec.server.dao;

import java.util.List;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
public class UserDao extends BaseDao<User> {


    public static void main(String[] args) {
        UserDao dao = new UserDao();
        dao.getAllUser().forEach(System.out::println);
    }

    public User findUserByName(String username) {
        return super.findDateBy(u -> u.getUsername().equals(username));
    }

    public List<User> getAllUser() {
        return mList;
    }
}
