package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public enum Method {
    GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS;
    public static Map<String, Method> map = new HashMap<>();

    static {
        for (Method method : Method.values()) {
            map.put(method.name(), method);
        }
    }
}
