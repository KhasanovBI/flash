package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public class Response {
    public static final String HTTP_VERSION = "HTTP/1.1";
    private StatusCode statusCode;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public Response(StatusCode statusCode) {
        setServerHeader();
        this.statusCode = statusCode;
    }

    public void setServerHeader() {
        headers.put("Server", "Flash 0.1.0");
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
