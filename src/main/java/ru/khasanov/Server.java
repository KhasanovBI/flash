package ru.khasanov;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by bulat on 06.01.17.
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Settings settings;

    Server(Settings settings) {
        this.settings = settings;
    }

    public void start() {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            SocketAddress socketAddress = new InetSocketAddress("localhost", settings.port);
            serverSocketChannel.socket().bind(socketAddress);
            serverSocketChannel.register(selector, serverSocketChannel.validOps());
            logger.info("Started Flash server at 0.0.0.0:" + settings.port);


            while (selector.select() > -1) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
