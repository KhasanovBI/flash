package ru.khasanov.exceptions;

import ru.khasanov.http.StatusCode;

/**
 * Created by bulat on 07.01.17.
 */
public abstract class FlashException extends RuntimeException {
    protected StatusCode statusCode;

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
