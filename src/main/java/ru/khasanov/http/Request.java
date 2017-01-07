package ru.khasanov.http;

/**
 * Created by bulat on 06.01.17.
 */
public class Request {
    private String rawString;
    public Request(String rawString) {
        this.rawString = rawString;
    }
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

//    TODO
    private Method method = Method.GET;
}
