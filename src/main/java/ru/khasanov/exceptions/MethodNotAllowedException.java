package ru.khasanov.exceptions;

import ru.khasanov.http.StatusCode;

/**
 * Created by bulat on 07.01.17.
 */
public class MethodNotAllowedException extends FlashHTTPException {
    public MethodNotAllowedException() {
        super();
        statusCode = StatusCode._405;
    }

}
