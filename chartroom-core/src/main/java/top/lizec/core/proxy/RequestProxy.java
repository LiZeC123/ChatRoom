package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;

public class RequestProxy implements MethodInterceptor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String serverName;
    private String prefixPath;
    private ObjectSocket socket;

    public RequestProxy(String serverName, String prefixPath) throws IOException {
        this.serverName = serverName;
        this.prefixPath = prefixPath;
        this.socket = new ObjectSocket(new Socket("localhost", 8848));
    }

    public Object newInstall(Class<?> clazz) {
        return Enhancer.create(clazz, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String path;
        for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
            if (declaredAnnotation instanceof GetMapping) {
                path = prefixPath + ((GetMapping) declaredAnnotation).value();

                LSTPEntityRequest request = LSTPEntityRequest.createGetWith(path, mapper.writeValueAsString(objects[0]));
                socket.writeUTF(request.toString());
                String result = socket.readUTF();

                LSTPEntityResponse response = LSTPEntityResponse.parseFrom(result);

                return decodeReturnValue(method, response);
            }
        }
        System.out.println("do RequestProxy");

        return methodProxy.invokeSuper(o, objects);
    }

    private Object decodeReturnValue(Method method, LSTPEntityResponse response) throws Throwable {
        if (method.getReturnType().getTypeName().equals(List.class.getTypeName())) {
            Type genericReturnType = method.getGenericReturnType();
            Type t = ((ParameterizedType) (genericReturnType)).getActualTypeArguments()[0];
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, (Class) t);
            return mapper.readValue(response.getBody(), javaType);
        } else {
            return mapper.readValue(response.getBody(), method.getReturnType());
        }

    }


}
