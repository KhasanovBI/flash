package http;

/**
 * Created by bulat on 05.01.17.
 */
public enum ContentType {
    // TODO несколько расширений на один контент-тайп
    HTML("html", "text/html"),
    JS("js", "application/javascript"),
    JPEG("jpeg", "image/jpeg");

    ContentType(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }
    private final String extension;
    private final String contentType;
}
