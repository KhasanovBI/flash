package ru.khasanov.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public final class ResponseRenderer {
    private static final Logger logger = LoggerFactory.getLogger(ResponseRenderer.class);

    public static final String DIVIDER = "\r\n";

    private static void appendStatusLine(StringBuilder stringBuilder, Response response) {
        StatusCode statusCode = response.getStatusCode();
        stringBuilder.append(HTTPVersion.HTTP_1_1.getVersion()).append(" ").append(statusCode.getCode()).append(" ")
                .append(statusCode.getMessage()).append(DIVIDER);

    }

    private static void appendHeaders(StringBuilder stringBuilder, Response response) {
        for (Map.Entry<ResponseHeader, String> header : response.getHeaders().entrySet()) {
            stringBuilder.append(header.getKey().getHeaderName()).append(": ").append(header.getValue())
                    .append(DIVIDER);
        }
        stringBuilder.append(DIVIDER);
    }

    public static ByteBuffer build(Response response) {
        StringBuilder stringBuilder = new StringBuilder();
        appendStatusLine(stringBuilder, response);
        appendHeaders(stringBuilder, response);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(stringBuilder.toString().getBytes());
            byte[] body = response.getBody();
            if (body != null) {
                outputStream.write(body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return ByteBuffer.wrap(outputStream.toByteArray());
    }
}
