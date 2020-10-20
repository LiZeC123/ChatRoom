package top.lizec.core.proxy;

import java.util.HashMap;

public class Context {
    private final HashMap<Class<?>, Object> automatiqueList;
    private final HashMap<String, String> valueList;
    private final HashMap<String, Object> objectList;

    public Context(HashMap<Class<?>, Object> automatiqueList, HashMap<String, String> valueList) {
        this.automatiqueList = automatiqueList;
        this.valueList = valueList;
        this.objectList = new HashMap<>();
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


    @SuppressWarnings("unchecked")
    public <T> T getObjectByName(String name) {
        return (T) objectList.get(name);
    }

    public void setObject(String name, Object object) {
        objectList.put(name, object);
    }
}
