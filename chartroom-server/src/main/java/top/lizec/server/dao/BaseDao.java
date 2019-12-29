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

public class BaseDao<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    protected List<T> mList;

    BaseDao() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(getTClass().getSimpleName() + ".data")));
            String jsonStr = in.readUTF();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, getTClass());
            mList = mapper.readValue(jsonStr, javaType);
        } catch (FileNotFoundException e) {
            mList = new ArrayList<>();
        } catch (IOException e) {
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

    private void saveToFile() {
        try {
            File file = new File(getTClass().getSimpleName() + ".data");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            System.out.println(mapper.writeValueAsString(mList));
            out.writeUTF(mapper.writeValueAsString(mList));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
