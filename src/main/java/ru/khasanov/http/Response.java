package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public class Response {
    private StatusCode statusCode;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();

    public Response(StatusCode statusCode, byte[] body) {
        this(statusCode);
        this.body = body;
    }

    public Response(StatusCode statusCode) {
        setDefaultHeaders();
        this.statusCode = statusCode;
    }

    public void setDefaultHeaders() {
        setServerHeader();
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

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
