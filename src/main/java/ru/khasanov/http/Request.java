package ru.khasanov.http;

/**
 * Created by bulat on 06.01.17.
 */
public class Request {
    //    TODO
    private Method method = Method.GET;

    public Request() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

}
