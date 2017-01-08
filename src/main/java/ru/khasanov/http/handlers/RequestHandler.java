package ru.khasanov.http.handlers;

import ru.khasanov.exceptions.FlashHTTPException;
import ru.khasanov.exceptions.MethodNotAllowedException;
import ru.khasanov.http.Request;
import ru.khasanov.http.Response;


/**
 * Created by bulat on 07.01.17.
 */
public class RequestHandler {
    public Response dispatch(Request request) {
        Response response;
        try {
            switch (request.getMethod()) {
                case GET:
                    response = get(request);
                    break;
                case POST:
                    response = post(request);
                    break;
                case PATCH:
                    response = patch(request);
                    break;
                case PUT:
                    response = put(request);
                    break;
                case DELETE:
                    response = delete(request);
                    break;
                case HEAD:
                    response = head(request);
                    break;
                case OPTIONS:
                    response = options(request);
                    break;
                default:
                    throw new MethodNotAllowedException();
            }
        } catch (FlashHTTPException e) {
            response = handleException(e);
        }
        return response;
    }

    protected Response handleException(FlashHTTPException e) {
        return new Response(e.getStatusCode());
    }

    public Response get(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response post(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response put(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response patch(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response delete(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response head(Request request) {
        throw new MethodNotAllowedException();
    }

    public Response options(Request request) {
        throw new MethodNotAllowedException();
    }
}
