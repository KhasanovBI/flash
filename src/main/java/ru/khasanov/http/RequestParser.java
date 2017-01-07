package ru.khasanov.http;

import ru.khasanov.exceptions.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bulat on 07.01.17.
 */
public class RequestParser {


    private static final Pattern requestLineRegex = Pattern.compile(
            "^([a-zA-Z]+) ([\\w\\/\\.?=]+) (HTTP\\/[0-2\\.]+)$");

    private static void parseRequestLine(Request request, String requestLineString) {
        Matcher matcher = requestLineRegex.matcher(requestLineString);
        if (matcher.find()) {
            String methodName = matcher.group(1).toUpperCase();
            Method method = Method.map.get(methodName);
            String httpVersionString = matcher.group(3);
            HTTPVersion httpVersion = HTTPVersion.map.get(httpVersionString);
            if (method == null || httpVersion == null) {
                throw new BadRequestException();
            }
            request.setHttpVersion(httpVersion);
            request.setMethod(method);
            String requestURI = matcher.group(2);
            request.setRequestURI(requestURI);
        } else {
            throw new BadRequestException();
        }
    }

    public static Request parse(String requestString) {
        Request request = new Request();
        BufferedReader bufferedReader = new BufferedReader(new StringReader(requestString));
        String requestLine;
        try {
            requestLine = bufferedReader.readLine();
            parseRequestLine(request, requestLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }
}
