package ru.khasanov.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public class Response {
    private StatusCode statusCode;
    private byte[] body;
    private Map<ResponseHeader, String> headers = new HashMap<>();

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
        headers.put(ResponseHeader.SERVER, "Flash 0.1.0");
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

    public Map<ResponseHeader, String> getHeaders() {
        return headers;
    }

    public void setHeader(ResponseHeader header, String value) {
        this.headers.put(header, value);
    }
}
