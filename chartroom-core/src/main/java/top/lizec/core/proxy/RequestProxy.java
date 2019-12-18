package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.Socket;

import top.lizec.core.annotation.GetMapping;
import top.lizec.core.entity.LSTPEntityRequest;
import top.lizec.core.entity.LSTPEntityResponse;

public class RequestProxy implements MethodInterceptor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String serverName;
    private String prefixPath;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public RequestProxy(String serverName, String prefixPath) {
        this.serverName = serverName;
        this.prefixPath = prefixPath;

    }

    public Object newInstall(Class<?> clazz) {
        return Enhancer.create(clazz, this);
    }

    private void initSocket() throws IOException {
        if (this.out == null) {
            synchronized (this) {
                if (this.out == null) {
                    Socket socket = new Socket("localhost", 8848);
                    this.out = new ObjectOutputStream(socket.getOutputStream());
                    this.in = new ObjectInputStream(socket.getInputStream());
                }
            }
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String path;
        for (Annotation declaredAnnotation : method.getDeclaredAnnotations()) {
            if (declaredAnnotation instanceof GetMapping) {
                // 第一次使用的时候才初始化Socket
                initSocket();
                path = prefixPath + ((GetMapping) declaredAnnotation).value();

                LSTPEntityRequest request = LSTPEntityRequest.createGetWith(path, mapper.writeValueAsString(objects[0]));

                out.writeUTF(request.toString());
                out.flush();

                String result = in.readUTF();
                LSTPEntityResponse response = LSTPEntityResponse.parseFrom(result);
                return mapper.readValue(response.getBody(), method.getReturnType());
            }
        }
        System.out.println("do RequestProxy");

        return methodProxy.invokeSuper(o, objects);
    }
}
