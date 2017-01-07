package ru.khasanov.http.handlers;

import ru.khasanov.exceptions.NotFoundException;
import ru.khasanov.http.Request;
import ru.khasanov.http.Response;
import ru.khasanov.http.StatusCode;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bulat on 07.01.17.
 */
public class StaticRequestHandler extends RequestHandler {
    private String rootDirectoryPath;

    public void initialize(Object... objects) {
        this.rootDirectoryPath = (String) objects[0];
    }

    public String stripFirstSlash(String requestURIString) {
        int slashIndex = requestURIString.indexOf('/');
        if (slashIndex == 1) {
            return requestURIString.substring(slashIndex);
        }
        return requestURIString;
    }

    public Response get(Request request) {
        URI requestURI = request.getRequestURI();
        String requestURIString = requestURI.toString();
        String stripRequestURIString = stripFirstSlash(requestURIString);
        try {
            Path filePath = Paths.get(this.rootDirectoryPath, stripRequestURIString);
            if (!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS) ||
                    Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
//                TODO
                throw new IOException();
            }
            byte[] body = Files.readAllBytes(filePath);
            return new Response(StatusCode._200, body);
        } catch (IOException e) {
//            TODO log
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
