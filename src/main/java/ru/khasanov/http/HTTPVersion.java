package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public enum HTTPVersion {
    HTTP_1_1("HTTP/1.1");

    HTTPVersion(String version) {
        this.version = version;
    }

    private String version;
    public static Map<String, HTTPVersion> map = new HashMap<>();

    static {
        for (HTTPVersion httpVersion : HTTPVersion.values()) {
            map.put(httpVersion.version, httpVersion);
        }
    }

    public String getVersion() {
        return version;
    }
}
