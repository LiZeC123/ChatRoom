package top.lizec.core.lstp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;

public class LSTPReceive {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Socket socket;
    private HashMap<String, Object> receiver;
    private HashMap<String, Method> pathList;

    public LSTPReceive(Socket socket, HashMap<String, Object> receiver, HashMap<String, Method> pathList) {
        this.socket = socket;
        this.receiver = receiver;
        this.pathList = pathList;
        startIOThread();
    }

    private static Class<?> getParameterType(Method method) {
        for (Class<?> type : method.getParameterTypes()) {
            return type;
        }
        return null;
    }

    private static <T> Object tryParseBodyWith(String body, Class<T> clazz) {
        try {
            return mapper.readValue(body, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    private void startIOThread() {
        new Thread(() -> {
            // 对socket加锁, 保证只有一个线程写入数据
            synchronized (socket) {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    while (!Thread.interrupted()) {
                        // 可以直接将报文封装成String, 然后发送和接受String
                        String info = in.readUTF();
                        System.out.println("Received Request:");
                        System.out.println(info);
                        LSTPEntityRequest request = LSTPEntityRequest.parseFrom(info);
                        if (receiver.containsKey(request.getPath())) {
                            Object ins = receiver.get(request.getPath());
                            Method method = pathList.get(request.getPath());

                            Class<?> type = getParameterType(method);
                            if (type == null) {
                                Object object = method.invoke(ins);
                                LSTPEntityResponse response = new LSTPEntityResponse("200", mapper.writeValueAsString(object));
                                out.writeUTF(response.toString());
                                out.flush();
                            } else {
                                Object param = tryParseBodyWith(request.getBody(), type);
                                Object object = method.invoke(ins, param);
                                LSTPEntityResponse response = new LSTPEntityResponse("200", mapper.writeValueAsString(object));
                                out.writeUTF(response.toString());
                                out.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // 类似HTTP的结构, 但是完全可以双向发送请求, 客户端可以请求服务器端, 服务器端也可以请求客户端
    // 处理请求可以是根据注解的被动调用
    // 发送请求的操作无法屏蔽网络细节, 需要IP地址=> Socket标识, 请求地址等内容

    // 目前服务器这边只需要一个广播的功能, 其他的内容都是客户端请求服务器端
}
