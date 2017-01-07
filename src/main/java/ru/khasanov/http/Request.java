package ru.khasanov.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bulat on 06.01.17.
 */
public class Request {
    private Method method;
    private URI requestURI;
    private HTTPVersion httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public URI getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(URI requestURI) {
        this.requestURI = requestURI;
    }

    public HTTPVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HTTPVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHeadersMap() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
