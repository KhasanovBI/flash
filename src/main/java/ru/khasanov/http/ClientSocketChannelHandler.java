package ru.khasanov.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by bulat on 07.01.17.
 */
public class ClientSocketChannelHandler {
    public static final int BUFFER_SIZE = 32768;
    private SocketChannel clientSocketChannel;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    public ByteArrayOutputStream getRequestByteArrayOutputStream() {
        return requestByteArrayOutputStream;
    }

    private ByteArrayOutputStream requestByteArrayOutputStream = new ByteArrayOutputStream();


    public ClientSocketChannelHandler(SocketChannel clientSocketChannel) {
        this.clientSocketChannel = clientSocketChannel;
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
}
