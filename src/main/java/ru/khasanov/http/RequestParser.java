package ru.khasanov.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bulat on 07.01.17.
 */
public class RequestParser {
    private Method method;
    private static final Pattern requestLineRegex = Pattern.compile(
            "^([A-Za-z]+) ([\\w\\/\\.?=]+) (HTTP\\/[0-2\\.]{3})\r\n$", Pattern.CASE_INSENSITIVE);
    public void parseRequestLine(String requestLineString) {
        Matcher matcher = requestLineRegex.matcher(requestLineString);
    }
    public static Request parse(String requestString) {
        Request request = new Request();

        return request;
    }
}
