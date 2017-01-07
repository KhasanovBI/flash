package ru.khasanov.http;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public class Response {
    public static final String HTTP_VERSION = "HTTP/1.1";
    private StatusCode statusCode;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();

    public Response(StatusCode statusCode) {
        setServerHeader();
        this.statusCode = statusCode;
//        TODO
        try {
            body = Files.readAllBytes(Paths.get("root_dir/cute.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
