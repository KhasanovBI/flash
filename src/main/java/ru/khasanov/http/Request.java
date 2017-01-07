package ru.khasanov.http;

/**
 * Created by bulat on 06.01.17.
 */
public class Request {
    private Method method;
    private String requestURI;
    private HTTPVersion httpVersion;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public HTTPVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HTTPVersion httpVersion) {
        this.httpVersion = httpVersion;
    }
}
