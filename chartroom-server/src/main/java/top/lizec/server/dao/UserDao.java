package top.lizec.server.dao;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import top.lizec.core.annotation.Component;
import top.lizec.core.biz.User;

@Component
public class UserDao {
    private final ObjectMapper mapper = new ObjectMapper();
    private List<User> userList;
    private List<User> onlineList;


    public UserDao() throws Exception {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("user.data")));
        String jsonStr = in.readUTF();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
        userList = mapper.readValue(jsonStr, javaType);
        onlineList = new ArrayList<>();
    }

    public UserDao(boolean init) {
        userList = new ArrayList<>();
        onlineList = new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
//        UserDao dao = new UserDao(false);
//        User user = new User("LiZeC", "123321");
//        User ggboy = new User("ggBoy", "654321");
//        dao.addNewUser(user);
//        dao.addNewUser(ggboy);

        UserDao dao = new UserDao();
        dao.getAllUser().forEach(System.out::println);
    }

    public void addNewUser(User user) {
        if (!hasUser(user)) {
            userList.add(user);
            saveToFile();
        }
    }

    public boolean hasUser(User user) {
        return userList.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    public List<User> getAllUser() {
        return userList;
    }

    public void insertOnLineUser() {

    }

    private void saveToFile() {
        try {
            File file = new File("user.data");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            System.out.println(mapper.writeValueAsString(userList));
            out.writeUTF(mapper.writeValueAsString(userList));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
