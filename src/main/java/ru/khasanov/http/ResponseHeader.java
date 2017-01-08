package ru.khasanov.http;

/**
 * Created by bulat on 07.01.17.
 */
public enum ResponseHeader {
    CONTENT_TYPE("Content-Type"),
    SERVER("Server"),
    CONTENT_LENGTH("Content-Length");

    ResponseHeader(String headerName) {
        this.headerName = headerName;
    }

    private String headerName;

    public String getHeaderName() {
        return headerName;
    }
}
