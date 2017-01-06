package ru.khasanov;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * Created by bulat on 05.01.17.
 */
public class Main {
    public static void main(String[] args) {
        Settings settings = new Settings();
        CmdLineParser cmdLineParser = new CmdLineParser(settings);
        try {
            cmdLineParser.parseArgument(args);
            Server server = new Server(settings);
            server.start();
        } catch (CmdLineException e) {
            System.err.println(e.getLocalizedMessage());
            cmdLineParser.printUsage(System.out);
        }
    }
}
