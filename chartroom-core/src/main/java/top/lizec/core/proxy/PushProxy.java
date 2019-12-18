package top.lizec.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class PushProxy implements MethodInterceptor {
    private static final ObjectMapper mapper = new ObjectMapper();
    private String serverName;
    private String prefixPath;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public PushProxy(String serverName, String prefixPath) {
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
        System.out.println("do Push");
        return null;
    }
}
