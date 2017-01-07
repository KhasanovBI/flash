package ru.khasanov;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.exceptions.BadRequestException;
import ru.khasanov.http.*;
import ru.khasanov.http.handlers.StaticRequestHandler;
import settings.Settings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by bulat on 06.01.17.
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Settings settings;

    Server(Settings settings) {
        this.settings = settings;
    }

    private void accept(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        if (settings.isLoggingEnable()) {
            logger.info("Connection Accepted: " + clientSocketChannel.getLocalAddress());
        }
    }

    private void read(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        ClientSocketChannelHandler handler = (ClientSocketChannelHandler) selectionKey.attachment();
        if (handler == null) {
            handler = new ClientSocketChannelHandler(clientSocketChannel);
            selectionKey.attach(handler);
        }
        if (handler.read()) {
            final String rawString = handler.getRequestByteArrayOutputStream().toString();
            logger.info(rawString);
            Response response;
            try {
                Request request = RequestParser.parse(rawString);
                response = new StaticRequestHandler().dispatch(request);
            } catch (BadRequestException e) {
                response = new Response(e.getStatusCode());
            }
            clientSocketChannel.write(ResponseRenderer.build(response));
            clientSocketChannel.close();
        }
    }

    public void write(SelectionKey selectionKey) {

    }

    public void connect(SelectionKey selectionKey) {

    }

    public void start() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            SocketAddress socketAddress = new InetSocketAddress("localhost", settings.getPort());
            serverSocketChannel.socket().bind(socketAddress);
            serverSocketChannel.register(selector, serverSocketChannel.validOps());
            logger.info("Started Flash server at " + socketAddress);
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        accept(selectionKey);
                    } else if (selectionKey.isReadable()) {
                        read(selectionKey);
                    } else if (selectionKey.isWritable()) {
                        write(selectionKey);
                    } else if (selectionKey.isConnectable()) {
                        connect(selectionKey);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
