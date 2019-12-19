package top.lizec.core.proxy;

import java.util.HashMap;

public class Context {
    private HashMap<Class<?>, Object> automatiqueList;
    private PushSocketManager manager;

    public Context(HashMap<Class<?>, Object> automatiqueList, PushSocketManager manager) {
        this.automatiqueList = automatiqueList;
        this.manager = manager;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjectByType(Class<T> clazz) {
        if (automatiqueList.containsKey(clazz)) {
            return (T) automatiqueList.get(clazz);
        }
        return null;
    }

}
