package ru.khasanov.http;

import ru.khasanov.Server;

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
        setBody(body);
    }

    public Response(StatusCode statusCode) {
        setDefaultHeaders();
        this.statusCode = statusCode;
    }

    public void setDefaultHeaders() {
        setServerHeader();
    }

    public void setServerHeader() {
        headers.put(ResponseHeader.SERVER, String.format("%s/%s", Server.NAME, Server.VERSION));
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
        setContentLengthHeader(body.length);
    }

    private void setContentLengthHeader(int contentLength) {
        headers.put(ResponseHeader.CONTENT_LENGTH, String.valueOf(contentLength));
    }

    public Map<ResponseHeader, String> getHeaders() {
        return headers;
    }

    public void setHeader(ResponseHeader header, String value) {
        this.headers.put(header, value);
    }
}
