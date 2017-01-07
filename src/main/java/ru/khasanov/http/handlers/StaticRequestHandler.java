package ru.khasanov.http.handlers;

import ru.khasanov.http.Request;
import ru.khasanov.http.Response;
import ru.khasanov.http.StatusCode;

/**
 * Created by bulat on 07.01.17.
 */
public class StaticRequestHandler extends RequestHandler {
    public Response get(Request request) {
        return new Response(StatusCode._200);
    }
}
