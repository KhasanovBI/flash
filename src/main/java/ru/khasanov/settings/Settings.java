package ru.khasanov.settings;

import com.beust.jcommander.Parameter;
import ru.khasanov.settings.validators.DirectoryExistsValidator;
import ru.khasanov.settings.validators.PortValidator;

/**
 * Created by bulat on 06.01.17.
 */
public class Settings {
    @Parameter(names = {"-h", "--help"}, help = true, description = "Display help information")
    private boolean help = false;
    @Parameter(names = {"-c", "--cache"}, arity = 1, description = "Enable cache using")
    private boolean cacheEnable = true;
    @Parameter(names = {"-r", "--root-directory"}, description = "Set root directory path",
            validateWith = DirectoryExistsValidator.class)
    private String rootDirectory = "root_dir";
    @Parameter(names = {"-p", "--port"}, description = "Set port", validateWith = PortValidator.class)
    private int port = 8080;
    @Parameter(names = {"-l", "--logging-enable"}, arity = 1, description = "Enable logging")
    private boolean loggingEnable = true;
    public boolean isHelp() {
        return help;
    }
    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public boolean isLoggingEnable() {
        return loggingEnable;
    }

    public int getPort() {
        return port;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }
}
