package top.lizec.core;

import org.reflections.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.annotation.ReceiveScan;
import top.lizec.core.annotation.RequestScan;
import top.lizec.core.lstp.LSTPReceive;

public class LZApplication {
    // 服务器端只需要群发消息, 客户端需要请求服务器端, 需要这个接口
    // 1. 扫描Request的注解
    // 2. 根据注解实现动态代理
    // 3. 扫描需要注入Request的类, 通过反射注入动态代理对象
    // 4. 客户端代码调用时 -> 调用代理方法 -> 使用协议包裹参数和返回值
    // IOC      https://www.cnblogs.com/lijiasnong/p/8398734.html
    // 动态代理 https://www.cnblogs.com/socketqiang/p/11212029.html
    // 需要产生如下的几个类
    // 1. socket消息接收类, 处理请求, 然后调用指定的方法
    // 2. socket消息发送类, 指定方法被调用时, 发送请求, 可以缓存socket
    // 3. ServerSocket类, 服务器端需要这个类监听请求, 以及群发任务


    private static HashMap<String, Object> pathController = new HashMap<>();
    private static HashMap<String, Method> pathMethod = new HashMap<>();

    public static void run(Class app) {
        System.out.println("LZApplication");
        //System.out.println(app);
        for (Annotation annotation : app.getAnnotations()) {
            if (annotation instanceof ReceiveScan) {
                try {
                    scanReceive(((ReceiveScan) annotation).value());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (annotation instanceof RequestScan) {
                try {
                    scanRequest(((RequestScan) annotation).value());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            startServerSocket();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ServerSocket启动过程出现错误");
        }


    }

    private static void scanReceive(String scanPackage) throws Exception {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(ReceiveController.class);
        String prefix = "";
        for (Class<?> controller : set) {
            for (Annotation annotation : controller.getAnnotations()) {
                if (annotation instanceof ReceiveController) {
                    prefix = ((ReceiveController) annotation).value();
                }
            }
            createReceiveMap(controller, prefix, pathController, pathMethod);
        }
    }

    private static void createReceiveMap(Class<?> controller, String prefix, HashMap<String, Object> pathController, HashMap<String, Method> pathMethod) throws Exception {
        Object ins = controller.newInstance();
        for (Method method : controller.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof GetMapping) {
                    String path = prefix + ((GetMapping) annotation).value();
                    System.out.println("path is " + path);
                    pathController.put(path, ins);
                    pathMethod.put(path, method);

                }
            }
        }
    }

    private static void scanRequest(String scanPackage) throws Exception {

    }

    private static void startServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8848);
        while (true) {
            Socket socket = serverSocket.accept();
            new LSTPReceive(socket, pathController, pathMethod);
        }



    }
}
