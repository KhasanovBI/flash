package ru.khasanov;

import org.kohsuke.args4j.Option;

import java.io.Serializable;

/**
 * Created by bulat on 06.01.17.
 */
public class Settings implements Serializable {
    @Option(name = "-c", aliases = "--cache", usage = "Set cache using")
    boolean cache = true;
    @Option(name = "-r", aliases = "--root-directory", usage = "Set root directory")
    String rootDirectory = "root_dir";
    int port = 80;

    @Option(name = "-p", aliases = "--port", usage = "Set port")
    private void setPort(int port) {
        if (port > 0 && port < 65536) {
            this.port = port;
        }
    }
}
