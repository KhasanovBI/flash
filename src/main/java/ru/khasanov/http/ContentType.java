package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 05.01.17.
 */
public enum ContentType {
    APPLICATION_JAVASCRIPT("application/javascript", "js"),
    IMAGE_JPEG("image/jpeg", "jpeg", "jpg"),
    TEXT_HTML("text/html", "html", "htm"),
    TEXT_PLAIN("text/plain", "txt");

    ContentType(String type, String... extensions) {
        this.extensions = extensions;
        this.type = type;
    }

    public static Map<String, ContentType> map = new HashMap<>();

    static {
        for (ContentType contentType : ContentType.values()) {
            for (String extension : contentType.extensions) {
                map.put(extension, contentType);
            }
        }
    }

    private final String[] extensions;
    private final String type;

    public String getType() {
        return type;
    }
}
