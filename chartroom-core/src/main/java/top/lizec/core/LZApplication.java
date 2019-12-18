package top.lizec.core;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.LSTPServer;
import top.lizec.core.annotation.PushController;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.annotation.RequestController;
import top.lizec.core.lstp.LSTPReceive;
import top.lizec.core.proxy.PushProxy;
import top.lizec.core.proxy.RequestProxy;

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
    //      - 显然群发任务需要持有全部的Socket, 因此只能由这个类完成


    private HashMap<String, Object> pathController = new HashMap<>();
    private HashMap<String, Method> pathMethod = new HashMap<>();
    private HashMap<Class<?>, Object> automatiqueList = new HashMap<>();

    public static void run(Class app) {
        new LZApplication().runApp(app);
    }

    private void runApp(Class app) {
        System.out.println("LZApplication");
        //System.out.println(app);

        String autoScanPackage;
        for (Annotation annotation : app.getAnnotations()) {
            if (annotation instanceof AutomatiqueScan) {
                autoScanPackage = ((AutomatiqueScan) annotation).value();
                try {
                    scanReceive(autoScanPackage);
                    scanRequest(autoScanPackage);
                    scanPush(autoScanPackage);
                    scanComponent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (null != app.getAnnotation(LSTPServer.class)) {
            startServerSocket();
        }
    }

    private void scanReceive(String scanPackage) throws Exception {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(ReceiveController.class);
        String prefix = "";
        for (Class<?> controller : set) {
            for (Annotation annotation : controller.getAnnotations()) {
                if (annotation instanceof ReceiveController) {
                    prefix = ((ReceiveController) annotation).value();
                }
            }
            createReceiveMap(controller, prefix);
        }
    }

    private void createReceiveMap(Class<?> controller, String prefix) throws Exception {
        Object ins = controller.newInstance();
        automatiqueList.put(controller, ins);
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

    private void scanRequest(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestController.class);
        String prefix = "";

        for (Class<?> controller : set) {
            for (Annotation annotation : controller.getAnnotations()) {
                if (annotation instanceof RequestController)
                    prefix = ((RequestController) annotation).value();
            }

            RequestProxy proxy = new RequestProxy("chartroom", prefix);
            Object ins = proxy.newInstall(controller);
            automatiqueList.put(controller, ins);
        }

    }

    private void scanPush(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(PushController.class);

        for (Class<?> controller : set) {
            PushProxy proxy = new PushProxy("chartroom", "");
            Object ins = proxy.newInstall(controller);
            automatiqueList.put(controller, ins);
        }
    }

    private void scanComponent() throws Exception {
        for (Class<?> clazz : automatiqueList.keySet()) {
            System.out.println("automatiqueList:" + clazz);
            for (Field field : clazz.getDeclaredFields()) {
                System.out.println("    Field:" + field);
                for (Annotation annotation : field.getAnnotations()) {
                    System.out.println("        annotation:" + annotation);
                    if (annotation instanceof Automatique) {

                        if (automatiqueList.containsKey(field.getType())) {
                            field.setAccessible(true);
                            System.out.println("        automatique");
                            field.set(automatiqueList.get(clazz), automatiqueList.get(field.getType()));
                        } else {
                            throw new IllegalArgumentException("自动注入失败");
                        }
                    }
                }
            }
        }
    }

    private void startServerSocket() {
        new Thread(() -> {
            try {
                System.out.println("启动业务Server线程");
                ServerSocket serverSocket = new ServerSocket(8848);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new LSTPReceive(socket, pathController, pathMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("启动推送Server线程");
                ServerSocket serverSocket = new ServerSocket(8846);
                while (true) {
                    Socket socket = serverSocket.accept();
                    // 创建一个类收集socket, 当调用推送方法时, 向所有的socket推送
                    //new LSTPReceive(socket, pathController, pathMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
