package ru.itmo.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.client.clienInterfaces.ResponseListener;
import ru.itmo.lab.common.commonNet.Request;
import ru.itmo.lab.common.myExceptions.ConnectionException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkManager
{
    private SocketChannel channel;
    private ResponseReceiver receiver;
    private Logger logger = LoggerFactory.getLogger(NetworkManager.class);

    private final List<ResponseListener> listeners = new CopyOnWriteArrayList<>();

    public NetworkManager() {}

    public void setChannel( SocketChannel channel ) throws IOException
    {
        this.channel = channel;
        channel.configureBlocking(false);
        startReceiver();
    }

    public void addListener( ResponseListener listener )
    {
        if( !listeners.contains(listener) )
        {
            listeners.add(listener);
        }
    }

    public void removeListener( ResponseListener listener )
    {
        listeners.remove(listener);
    }

    private void startReceiver()
    {
        if( receiver != null )
            receiver.stop();
        receiver = new ResponseReceiver(channel, listeners);
        Thread receiverThread = new Thread(receiver);
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    public void sendRequest( Request request )
    {
        if( channel == null || !channel.isOpen() )
        {
            notifyError("Сетевой канал не доступен!");
            return;
        }
        CompletableFuture.runAsync( () -> {
            try
            {
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                try( ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream) )
                {
                    outputStream.writeObject(request);
                    outputStream.flush();
                }

                byte[] data = byteOutputStream.toByteArray();
                ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
                buffer.putInt(data.length);
                buffer.put(data);
                buffer.flip();

                while (buffer.hasRemaining())
                {
                    channel.write(buffer);
                }
            }
            catch (IOException e)
            {
                throw new ConnectionException("Сервер разрвал соединение.");
            }
        });
    }

    private void notifyError( String message )
    {
        for (ResponseListener listener : listeners) {
            listener.onError(message);
        }
    }
}
