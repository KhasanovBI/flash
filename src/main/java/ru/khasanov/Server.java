package ru.khasanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.http.handlers.RequestHandler;
import ru.khasanov.http.handlers.StaticRequestHandler;
import ru.khasanov.settings.Settings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bulat on 06.01.17.
 */
public class Server {
    public static final String NAME = "Flash";
    public static final String VERSION = "0.1.0";
    private static final Integer MIN_WORKER_COUNT = 2;
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Settings settings;
    private ExecutorService executor;
    private RequestHandler staticRequestHandler;

    public Server(Settings settings) {
        this.settings = settings;
        initExecutor(settings);
        staticRequestHandler = new StaticRequestHandler(settings.getRootDirectory(), settings.isCacheEnable());
    }

    private void initExecutor(Settings settings) {
        int cpuCount = Runtime.getRuntime().availableProcessors();
        // Вычитаем по 1 процессору на главный поток и на поток FileCacheWatcher, если кеш включен, и ограничиваемся
        // минимальным числом MIN_WORKER_COUNT
        int parallelism = cpuCount - 1;
        if (settings.isCacheEnable()) {
            --parallelism;
        }
        parallelism = Math.max(parallelism, MIN_WORKER_COUNT);
        executor = Executors.newFixedThreadPool(parallelism);
    }

    private void handleAccept(SelectionKey selectionKey) throws IOException {
        SocketChannel clientSocketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        clientSocketChannel.configureBlocking(false);
        clientSocketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        logger.debug("Connection Accepted: " + clientSocketChannel.getRemoteAddress());
    }

    private void handleRead(SelectionKey selectionKey) throws IOException {
        ClientSocketHandler handler = (ClientSocketHandler) selectionKey.attachment();
        if (handler == null) {
            handler = new ClientSocketHandler(selectionKey);
            selectionKey.attach(handler);
        }
        if (!handler.read()) {
            close(selectionKey);
            return;
        }
        final ClientSocketHandler finalHandler = handler;
        executor.submit(new Runnable() {
            @Override
            public void run() {
                finalHandler.process(staticRequestHandler);
            }
        });
    }

    private void close(SelectionKey selectionKey) throws IOException {
        selectionKey.cancel();
        selectionKey.channel().close();
    }

    private void handleWrite(SelectionKey selectionKey) throws IOException {
        ClientSocketHandler handler = (ClientSocketHandler) selectionKey.attachment();
        handler.write();
        close(selectionKey);
    }

    public ServerSocketChannel initServerSocketChannel() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        SocketAddress socketAddress = new InetSocketAddress("localhost", settings.getPort());
        serverSocketChannel.socket().bind(socketAddress);
        System.out.println(String.format("Started %s %s server at %s", NAME, VERSION, socketAddress));
        return serverSocketChannel;
    }

    public void start() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = initServerSocketChannel();
            serverSocketChannel.register(selector, serverSocketChannel.validOps());

            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isValid()) {
                        try {
                            if (selectionKey.isAcceptable()) {
                                handleAccept(selectionKey);
                            } else if (selectionKey.isReadable()) {
                                handleRead(selectionKey);
                            } else if (selectionKey.isWritable()) {
                                handleWrite(selectionKey);
                            }
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                            close(selectionKey);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
