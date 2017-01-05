package http;

/**
 * Created by bulat on 05.01.17.
 */
public enum StatusCode {
    _200(200, "OK"),
    _400(400, "Bad Request"),
    _404(404, "Not Found"),
    _405(405, "Method Not Allowed");

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private final int code;
    private final String message;
}
