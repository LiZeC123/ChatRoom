package top.lizec.server.dao;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import top.lizec.core.biz.Message;
import top.lizec.core.biz.User;
import top.lizec.server.entity.Friend;
import top.lizec.server.entity.MessageR;

public class BaseDao<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    List<T> mList;

    BaseDao() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("./database/" + getTClass().getSimpleName() + ".data")));
            String jsonStr = in.readUTF();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, getTClass());
            mList = mapper.readValue(jsonStr, javaType);
        } catch (FileNotFoundException e) {
            mList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("数据加载失败");
        }
    }


    public void insert(T data) {
        mList.add(data);
        saveToFile();
    }

    public void update(T data, Predicate<T> isMatch) {
        for (int i = 0; i < mList.size(); i++) {
            if (isMatch.test(mList.get(i))) {
                T old = mList.get(i);
                mList.remove(i);
                mList.add(data);
                System.out.println("Update" + old + "to" + data);
                break;
            }
        }
    }

    T findDateBy(Predicate<T> isMatch) {
        for (T t : mList) {
            if (isMatch.test(t)) {
                return t;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        FriendDao friendDao = new FriendDao();
        MessageDao messageDao = new MessageDao();

        userDao.mList.clear();
        friendDao.mList.clear();
        messageDao.mList.clear();

        userDao.insert(new User("lizec", "1"));
        userDao.insert(new User("ggboy", "1"));
        userDao.insert(new User("曹植", "1"));
        userDao.insert(new User("黄天赐", "1"));
        userDao.insert(new User("温浩", "1"));
        userDao.insert(new User("研究生网络学习", "1"));
        userDao.insert(new User("数传大水群", "1"));
        userDao.insert(new User("小老弟", "1"));
        userDao.insert(new User("老师甲", "1"));
        userDao.insert(new User("小学弟", "1"));
        userDao.insert(new User("Mr.Smith", "1"));

        // 所有人两两之间添加好友
        int length = userDao.mList.size();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                User u1 = userDao.mList.get(i);
                User u2 = userDao.mList.get(j);
                friendDao.insert(new Friend(u1.getUsername(), u2.getUsername()));
            }
        }

        Message message = new Message("lizec", null, "那真是太棒了", "ggboy");
        messageDao.insert(new MessageR(message));

        message.setContent("我觉得你可以再考虑一下");
        message.setReceiver("曹植");
        messageDao.insert(new MessageR(message));

        message.setUsername("黄天赐");
        message.setContent("我永远喜欢洛天依");
        message.setReceiver("lizec");
        messageDao.insert(new MessageR(message));
    }

    private void saveToFile() {
        try {
            File file = new File("./database/" + getTClass().getSimpleName() + ".data");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeUTF(mapper.writeValueAsString(mList));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
