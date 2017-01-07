package ru.khasanov;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import settings.Settings;

/**
 * Created by bulat on 05.01.17.
 */
public class Main {
    public static void main(String[] args) {
        Settings settings = new Settings();
        JCommander jCommander = new JCommander(settings);
        try {
            jCommander.parse(args);
            Server server = new Server(settings);
            server.start();
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            jCommander.usage();
        }
    }
}
