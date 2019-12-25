package top.lizec.core;

import org.reflections.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.AutomatiqueScan;
import top.lizec.core.annotation.Component;
import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.LSTPClient;
import top.lizec.core.annotation.LSTPServer;
import top.lizec.core.annotation.PushController;
import top.lizec.core.annotation.ReceiveController;
import top.lizec.core.annotation.RequestController;
import top.lizec.core.proxy.Context;
import top.lizec.core.proxy.LSTPReceive;
import top.lizec.core.proxy.PushProxy;
import top.lizec.core.proxy.PushSocketManager;
import top.lizec.core.proxy.RequestProxy;
import top.lizec.core.proxy.UserThread;
import top.lizec.core.security.CertManager;

public class LZApplication {


    private HashMap<String, Object> pathController = new HashMap<>();
    private HashMap<String, Method> pathMethod = new HashMap<>();
    private HashMap<Class<?>, Object> automatiqueList = new HashMap<>();
    private PushSocketManager manager = new PushSocketManager();
    private CertManager certManager;

    public static void run(Class app) {
        new LZApplication().runApp(app, null);
    }

    public static void run(Class app, UserThread mainThread) {
        new LZApplication().runApp(app, mainThread);
    }

    private void runApp(Class app, UserThread mainThread) {
        System.out.println("Start " + app.getSimpleName());

        String autoScanPackage;
        for (Annotation annotation : app.getAnnotations()) {
            if (annotation instanceof AutomatiqueScan) {
                autoScanPackage = ((AutomatiqueScan) annotation).value();
                try {
                    scanReceive(autoScanPackage);
                    scanRequest(autoScanPackage);
                    scanPush(autoScanPackage);
                    scanComponent(autoScanPackage);
                    autoSetComponent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (null != app.getAnnotation(LSTPServer.class)) {
            LSTPServer annotation = (LSTPServer) app.getAnnotation(LSTPServer.class);
            String serverName = annotation.name();
            loadCertManager(serverName);
            startServerSocket(serverName);
        }

        if (null != app.getAnnotation(LSTPClient.class)) {
            startClientSocket();
        }

        final Context context = new Context(automatiqueList, manager);
        if (null != mainThread) {
            System.out.println("Start User-Defined Main Thread");
            new Thread(() -> mainThread.run(context)).start();
        }
    }

    private void scanComponent(String scanPackage) throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Component.class);
        for (Class<?> controller : set) {
            for (Annotation annotation : controller.getAnnotations()) {
                if (annotation instanceof Component) {
                    automatiqueList.put(controller, controller.newInstance());
                }
            }
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
                    System.out.println("Extract Path: " + path);
                    pathController.put(path, ins);
                    pathMethod.put(path, method);
                }
            }
        }
    }

    private void scanRequest(String scanPackage) throws IOException {
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
            PushProxy proxy = new PushProxy("chartroom", "", manager);
            Object ins = proxy.newInstall(controller);
            automatiqueList.put(controller, ins);
        }
    }

    private void autoSetComponent() throws Exception {
        for (Class<?> clazz : automatiqueList.keySet()) {
            //System.out.println("automatiqueList:" + clazz);
            for (Field field : clazz.getDeclaredFields()) {
                //System.out.println("    Field:" + field);
                for (Annotation annotation : field.getAnnotations()) {
                    //System.out.println("        annotation:" + annotation);
                    if (annotation instanceof Automatique) {
                        if (automatiqueList.containsKey(field.getType())) {
                            field.setAccessible(true);
                            System.out.println("Automatically Inject " + field);
                            field.set(automatiqueList.get(clazz), automatiqueList.get(field.getType()));
                        } else {
                            throw new IllegalArgumentException("自动注入失败");
                        }
                    }
                }
            }
        }
    }

    private void loadCertManager(String serverName) {
        System.out.println("Load Certificate");
        this.certManager = new CertManager(serverName);
    }

    private void startServerSocket(String serverName) {
        new Thread(() -> {
            try {
                System.out.println("Start Work Thread For Server");
                ServerSocket serverSocket = new ServerSocket(8848);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new LSTPReceive(socket, serverName, certManager, pathController, pathMethod, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("Start Push Thread For Server");
                ServerSocket serverSocket = new ServerSocket(8846);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Push Thread Receive A New Socket Request From Client");
                    manager.addSocket(socket, serverName, certManager);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startClientSocket() {
        new Thread(() -> {
            try {
                System.out.println("Start Push Thread For Client");
                Socket socket = new Socket("localhost", 8846);
                new LSTPReceive(socket, null, null, pathController, pathMethod, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
