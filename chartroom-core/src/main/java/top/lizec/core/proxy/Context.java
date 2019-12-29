package top.lizec.core.proxy;

import java.util.HashMap;

public class Context {
    private HashMap<Class<?>, Object> automatiqueList;
    private PushSocketManager manager;
    private HashMap<String, String> valueList;

    public Context(HashMap<Class<?>, Object> automatiqueList, PushSocketManager manager, HashMap<String, String> valueList) {
        this.automatiqueList = automatiqueList;
        this.manager = manager;
        this.valueList = valueList;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjectByType(Class<T> clazz) {
        if (automatiqueList.containsKey(clazz)) {
            return (T) automatiqueList.get(clazz);
        }
        return null;
    }

    public String getValueByName(String name) {
        return valueList.get(name);
    }

    public void setValue(String name, String value) {
        valueList.put(name, value);
    }

}
