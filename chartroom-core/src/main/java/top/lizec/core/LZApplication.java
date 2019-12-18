package top.lizec.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.annotation.PostMapping;
import top.lizec.core.annotation.ReceiveScan;
import top.lizec.core.annotation.RequestController;

public class LZApplication {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void run(Class app) {
        System.out.println("LZApplication");
        System.out.println(app);
        for (Annotation annotation : app.getAnnotations()) {
            if (annotation instanceof ReceiveScan) {
                ReceiveScan receiveScan = (ReceiveScan) annotation;
                try {
                    scanReceive(receiveScan.value());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(annotation);
        }

    }

    private static void scanReceive(String scanPackage) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(RequestController.class);
        String prefix = "";
        for (Class<?> controller : set) {
            for (Annotation annotation : controller.getAnnotations()) {
                if (annotation instanceof RequestController) {
                    prefix = ((RequestController) annotation).value();
                }
            }

            Object ins = controller.newInstance();

            for (Method method : controller.getMethods()) {
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation instanceof GetMapping) {
                        String path = ((GetMapping) annotation).value();
                        System.out.println("path is " + prefix + path);
                        // 两个hash表, 一个保存path-object, 一个保存path-method
                        // 这样只要收到请求, 就可以完成调用
                        System.out.println(method.invoke(ins));

                        for (Class<?> type : method.getParameterTypes()) {
                            System.out.println(type);
                        }
                    }

                    if (annotation instanceof PostMapping) {
                        System.out.println("ParameterType is");
                        for (Class<?> type : method.getParameterTypes()) {
                            System.out.println(type);
                        }
                    }
                }

            }
        }
    }
}
