package ru.khasanov.http;

/**
 * Created by bulat on 05.01.17.
 */
public enum ContentType {
    APPLICATION_JAVASCRIPT("application/javascript", "js"),
    IMAGE_JPEG("image/jpeg", "jpeg", "jpg"),
    TEXT_HTML("text/html", "html", "htm"),
    TEXT_PLAIN("text/plain", "txt");

    ContentType(String contentType, String... extensions) {
        this.extensions = extensions;
        this.contentType = contentType;
    }

    private final String[] extensions;
    private final String contentType;
}
