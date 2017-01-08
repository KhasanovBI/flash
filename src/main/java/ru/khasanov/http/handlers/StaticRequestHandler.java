package ru.khasanov.http.handlers;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.exceptions.NotFoundException;
import ru.khasanov.http.*;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


/**
 * Created by bulat on 07.01.17.
 */
public class StaticRequestHandler extends RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticRequestHandler.class);
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
//        TODO парсинг
        String requestURIString = requestURI.toString();
        String stripRequestURIString = stripFirstSlash(requestURIString);
        Path filePath = Paths.get(this.rootDirectoryPath, stripRequestURIString);

        if (!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS) ||
                Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
            throw new NotFoundException();
        }
        byte[] body = null;
        try {
            body = Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        Response response = new Response(StatusCode._200, body);
//            TODO Move to Response
        response.setHeader(ResponseHeader.CONTENT_TYPE.getHeaderName(), getContentType(filePath));
        return response;
    }
}
