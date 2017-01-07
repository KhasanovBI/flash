package ru.khasanov.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bulat on 07.01.17.
 */
public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);
    //            TODO расширить символы URI
    private static final Pattern requestLineRegex = Pattern.compile(
            "^([a-zA-Z]+) ([\\w\\/\\.?=]+) (HTTP\\/[0-2\\.]+)$");

    private static void parseRequestLine(Request request, BufferedReader bufferedReader) throws IOException {
        String requestLine = bufferedReader.readLine();
        Matcher matcher = requestLineRegex.matcher(requestLine);
        if (matcher.find()) {
            String methodName = matcher.group(1).toUpperCase();
            Method method = Method.map.get(methodName);
            String httpVersionString = matcher.group(3);
            HTTPVersion httpVersion = HTTPVersion.map.get(httpVersionString);
            URI requestURI = null;
            try {
                requestURI = new URI(matcher.group(2));
            } catch (URISyntaxException ignored) {
            }
            if (method == null || httpVersion == null || requestURI == null) {
                logger.error("Invalid request line: " + requestLine);
                throw new ParseException();
            }
            request.setHttpVersion(httpVersion);
            request.setMethod(method);
            request.setRequestURI(requestURI);
        } else {
            throw new ParseException();
        }
    }

    private static void parseHeaders(Request request, BufferedReader bufferedReader) throws IOException {
        Map<String, String> headersMap = request.getHeadersMap();
        String rawHeader = bufferedReader.readLine();
        while (rawHeader.length() > 0) {
            String[] splitParts = rawHeader.split(":", 2);
            if (splitParts.length != 2) {
                logger.error("Invalid header: " + rawHeader);
                throw new ParseException();
            }
            headersMap.put(splitParts[0], splitParts[1]);
            rawHeader = bufferedReader.readLine();
        }
    }

    private static void parseBody(Request request, BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String bodyLine;
        while ((bodyLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(bodyLine).append(ResponseRenderer.DIVIDER);
        }
        request.setBody(stringBuilder.toString());
    }

    public static Request parse(String requestString) {
        Request request = new Request();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestString));
        try {
            parseRequestLine(request, bufferedReader);
            parseHeaders(request, bufferedReader);
            parseBody(request, bufferedReader);
        } catch (IOException e) {
            throw new ParseException();
        }
        return request;
    }
}
