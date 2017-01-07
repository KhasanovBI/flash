package ru.khasanov.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Created by bulat on 07.01.17.
 */
public final class ResponseRenderer {
    private static final String DIVIDER = "\r\n";

    private static void appendStatusLine(StringBuilder stringBuilder, Response response) {
        StatusCode statusCode = response.getStatusCode();
        stringBuilder.append(Response.HTTP_VERSION).append(" ").append(statusCode.getCode()).append(" ")
                .append(statusCode.getMessage()).append(DIVIDER);

    }

    private static void appendHeaders(StringBuilder stringBuilder, Response response) {
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            stringBuilder.append(header.getKey()).append(": ").append(header.getValue()).append(DIVIDER);
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
            outputStream.write(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ByteBuffer.wrap(outputStream.toByteArray());
    }
}
