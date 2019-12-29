package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import top.lizec.core.annotation.PushMapping;
import top.lizec.core.entity.LSTPEntityRequest;

public class PushProxy implements MethodInterceptor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String prefixPath;
    private PushSocketManager manager;

    public PushProxy(String prefixPath, PushSocketManager manager) {
        this.prefixPath = prefixPath;
        this.manager = manager;
    }

    public Object newInstall(Class<?> clazz) {
        return Enhancer.create(clazz, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Do Push on Method: " + method.getName());
        for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
            if (declaredAnnotation instanceof PushMapping) {
                String path = prefixPath + ((PushMapping) declaredAnnotation).value();

                LSTPEntityRequest request = LSTPEntityRequest.createGetWith(path, mapper.writeValueAsString(objects[0]));

                if (isList(method)) {
                    System.out.println("Send For List");

                    List<String> names = (List<String>) objects[1];
                    // for
                } else {
                    String username = (String) objects[1];
                    System.out.println("Send For User:" + username);
                    manager.pushTo(request, username);
                }
                // 这里一定要返回, 否则调用了下面的原有方法, 就会出错(因为没有)
                return null;
            }
        }

        // 如果不是需要拦截的方法, 则调用原有的方法
        return methodProxy.invokeSuper(o, objects);
    }

    private boolean isList(Method method) {
        return Arrays.stream(method.getParameterTypes()).anyMatch(type -> type.getTypeName().equals(List.class.getTypeName()));
    }
}
