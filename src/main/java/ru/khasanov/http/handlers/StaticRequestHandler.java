package ru.khasanov.http.handlers;

import org.apache.commons.io.FilenameUtils;
import ru.khasanov.exceptions.NotFoundException;
import ru.khasanov.http.*;

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

    public String getContentType(Path filePath) {
        String fileExtension = FilenameUtils.getExtension(filePath.toString());
        ContentType contentType = ContentType.map.get(fileExtension);
        if (contentType == null) {
            contentType = ContentType.TEXT_PLAIN;
        }
        return contentType.getType();
    }

    public Response get(Request request) {
        URI requestURI = request.getRequestURI();
//        TODO
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
            Response response = new Response(StatusCode._200, body);
//            TODO Move to Response
            response.setHeader(ResponseHeader.CONTENT_TYPE.getHeaderName(), getContentType(filePath));
            return response;
        } catch (IOException e) {
//            TODO log
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
}
