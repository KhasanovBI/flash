package ru.khasanov.exceptions;

import ru.khasanov.http.StatusCode;

/**
 * Created by bulat on 08.01.17.
 */
public class FlashHTTPException extends FlashException {
    protected StatusCode statusCode;

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
