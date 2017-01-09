package ru.khasanov.http.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.cache.FileCache;
import ru.khasanov.exceptions.NotFoundException;
import ru.khasanov.http.Request;
import ru.khasanov.http.Response;
import ru.khasanov.http.ResponseHeader;
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
    private static final Logger logger = LoggerFactory.getLogger(StaticRequestHandler.class);
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";
    private String rootDirectory;
    private FileCache fileCache;

    public StaticRequestHandler(String rootDirectory, boolean isCacheEnable) {
        this.rootDirectory = rootDirectory;
        // TODO DI
        if (isCacheEnable) {
            fileCache = new FileCache(rootDirectory);
        }
    }

    public String stripFirstSlash(String requestPath) {
        int slashIndex = requestPath.indexOf('/');
        if (slashIndex == 1) {
            return requestPath.substring(slashIndex);
        }
        return requestPath;
    }

    public String getContentType(Path filePath) {
        // Изначально здесь было ручное определение по расширению, если что можно вернуть.
        String contentType = null;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return contentType == null ? DEFAULT_CONTENT_TYPE : contentType;
    }

    public byte[] getFileContent(Path filePath) throws NotFoundException {
        byte[] body = null;
        if (fileCache != null) {
            body = fileCache.get(filePath);
        }
        if (body == null) {
            // В кэше не нашли, идем на диск
            if (!Files.exists(filePath, LinkOption.NOFOLLOW_LINKS) ||
                    Files.isDirectory(filePath, LinkOption.NOFOLLOW_LINKS)) {
                throw new NotFoundException();
            }
            try {
                body = Files.readAllBytes(filePath);
//                TODO двойная проверка
                if (fileCache != null) {
                    fileCache.set(filePath, body);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            logger.info(filePath + " найден в кэше.");
        }
        return body;
    }

    public Response get(Request request) {
        URI requestURI = request.getRequestURI();
        String requestPath = requestURI.getPath();
        String stripRequestURIString = stripFirstSlash(requestPath);
        Path filePath = Paths.get(this.rootDirectory, stripRequestURIString);
        Response response = new Response(StatusCode._200, getFileContent(filePath));
        response.setHeader(ResponseHeader.CONTENT_TYPE, getContentType(filePath));
        return response;
    }
}
