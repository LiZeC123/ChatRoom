package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;
import top.lizec.core.security.CertManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class LSTPReceive {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final ObjectSocket socket;
    private final HashMap<String, Object> receiver;
    private final HashMap<String, Method> pathList;
    private final boolean loopMode;

    public LSTPReceive(Socket socket, String serverName, CertManager certManager, HashMap<String, Object> receiver, HashMap<String, Method> pathList, boolean loopMode) throws IOException {
        this.socket = new ObjectSocket(socket, serverName, certManager);
        this.receiver = receiver;
        this.pathList = pathList;
        this.loopMode = loopMode;
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

    private void oneLoop() throws Exception {
        String info = socket.readUTF();
        LSTPEntityRequest request = LSTPEntityRequest.parseFrom(info);

        if (receiver.containsKey(request.getPath())) {
            System.out.println("Receive Request " + request.getPath());
            Object ins = receiver.get(request.getPath());
            Method method = pathList.get(request.getPath());

            Object result = invokeMethod(request, ins, method);

            LSTPEntityResponse response = new LSTPEntityResponse("200", mapper.writeValueAsString(result));
            socket.writeUTF(response.toString());
        } else {
            System.out.println("Miss The Request " + request.getPath());
        }
    }

    private Object invokeMethod(LSTPEntityRequest request, Object ins, Method method) throws IllegalAccessException, InvocationTargetException {
        Class<?> type = getParameterType(method);
        Object result;
        if (type == null) {
            result = method.invoke(ins);
        } else {
            Object param = tryParseBodyWith(request.getBody(), type);
            result = method.invoke(ins, param);
        }
        return result;
    }

    private void startIOThread() {
        Thread ioThread = new Thread(() -> {
            do {
                try {
                    oneLoop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (loopMode && !socket.hasClosed());
        });
        // 设置为守护线程, 从而在其他线程都结束时不再等待请求
        ioThread.setDaemon(true);
        ioThread.start();
    }


    // 类似HTTP的结构, 但是完全可以双向发送请求, 客户端可以请求服务器端, 服务器端也可以请求客户端
    // 处理请求可以是根据注解的被动调用
    // 发送请求的操作无法屏蔽网络细节, 需要IP地址=> Socket标识, 请求地址等内容

    // 目前服务器这边只需要一个广播的功能, 其他的内容都是客户端请求服务器端
}
