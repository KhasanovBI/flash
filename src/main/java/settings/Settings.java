package settings;

import com.beust.jcommander.Parameter;
import settings.validators.DirectoryExistsValidator;
import settings.validators.PortValidator;

/**
 * Created by bulat on 06.01.17.
 */
public class Settings {
    @Parameter(names = {"-c", "--cache"}, description = "Set cache using")
    boolean cache = true;
    @Parameter(names = {"-r", "--root-directory"}, description = "Set root directory path",
            validateWith = DirectoryExistsValidator.class)
    private String rootDirectoryPath = "root_dir";
    @Parameter(names = {"-p", "--port"}, description = "Set port", validateWith = PortValidator.class)
    private int port = 80;
    @Parameter(names = {"-l", "--loggingEnable"}, description = "Enable logging")
    private boolean loggingEnable = true;

    public boolean isLoggingEnable() {
        return loggingEnable;
    }

    public int getPort() {
        return port;
    }
}
