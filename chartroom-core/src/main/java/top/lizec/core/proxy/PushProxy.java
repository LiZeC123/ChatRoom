package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import top.lizec.core.annotation.PushMapping;
import top.lizec.core.entity.LSTPEntityRequest;

public class PushProxy implements MethodInterceptor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String serverName;
    private String prefixPath;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private PushSocketManager manager;

    public PushProxy(String serverName, String prefixPath, PushSocketManager manager) {
        this.serverName = serverName;
        this.prefixPath = prefixPath;
        this.manager = manager;
    }

    public Object newInstall(Class<?> clazz) {
        return Enhancer.create(clazz, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("do Push");
        String path;
        for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
            if (declaredAnnotation instanceof PushMapping) {
                path = prefixPath + ((PushMapping) declaredAnnotation).value();

                LSTPEntityRequest request = LSTPEntityRequest.createGetWith(path, mapper.writeValueAsString(objects[0]));
                manager.pushMessage(request);
                // 这里一定要返回, 否则调用了下面的原有方法, 就会出错(因为没有)
                return null;
            }
        }

        return methodProxy.invokeSuper(o, objects);
    }
}
