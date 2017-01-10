package ru.khasanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khasanov.exceptions.ParseException;
import ru.khasanov.http.*;
import ru.khasanov.http.handlers.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by bulat on 07.01.17.
 */
public class ClientSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientSocketHandler.class);
    private static final int BUFFER_SIZE = 8000;
    private final SelectionKey selectionKey;
    private SocketChannel clientSocketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer responseBytes;
    private ByteArrayOutputStream requestByteArrayOutputStream = new ByteArrayOutputStream();

    public ClientSocketHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.clientSocketChannel = (SocketChannel) selectionKey.channel();
    }

    public boolean read() throws IOException {
        int readNumber;
        while (true) {
            readNumber = clientSocketChannel.read(byteBuffer);
            if (readNumber < 0) {
                return false;
            } else if (readNumber == 0) {
                return true;
            }
            requestByteArrayOutputStream.write(byteBuffer.array(), 0, byteBuffer.position());
            byteBuffer.clear();
        }
    }

    public void process(RequestHandler requestHandler) {
        final String rawString = requestByteArrayOutputStream.toString();
        logger.info(rawString);
        Response response;
        try {
            Request request = RequestParser.parse(rawString);
            response = requestHandler.dispatch(request);
        } catch (ParseException e) {
            response = new Response(StatusCode._400);
        }
        responseBytes = ResponseRenderer.build(response);
        // Set the key's interest to WRITE operation
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        selectionKey.selector().wakeup();
    }

    public void write() throws IOException {
        int numBytes = clientSocketChannel.write(responseBytes);
        if (numBytes > 0) {
            selectionKey.interestOps(SelectionKey.OP_READ);
            selectionKey.selector().wakeup();
        }
    }
}
