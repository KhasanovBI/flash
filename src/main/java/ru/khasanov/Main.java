package ru.khasanov;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import ru.khasanov.settings.Settings;

/**
 * Created by bulat on 05.01.17.
 */
public class Main {
    private static void disableLogging() {
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration config = loggerContext.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.OFF);
        loggerContext.updateLoggers();
    }

    public static void main(String[] args) {
        Settings settings = new Settings();
        JCommander jCommander = new JCommander(settings);
        jCommander.setProgramName(Server.NAME);
        try {
            jCommander.parse(args);
            if (settings.isHelp()) {
                jCommander.usage();
                return;
            }
            if (!settings.isLoggingEnable()) {
                disableLogging();
            }
            Server server = new Server(settings);
            server.start();
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jCommander.usage();
        }
    }
}
