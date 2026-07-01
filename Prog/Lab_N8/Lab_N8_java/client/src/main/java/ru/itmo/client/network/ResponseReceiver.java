package ru.itmo.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.client.clienInterfaces.ResponseListener;
import ru.itmo.lab.common.commonNet.AbstractResponse;
import ru.itmo.lab.common.commonNet.ResponseCmdRes;
import ru.itmo.lab.common.commonNet.UpdateNotification;

import javax.swing.SwingUtilities;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

public class ResponseReceiver implements Runnable
{
    private final Logger logger = LoggerFactory.getLogger(ResponseReceiver.class);
    private final SocketChannel channel;
    private final List<ResponseListener> listeners;
    private volatile boolean isRunning = true;

    public ResponseReceiver( SocketChannel channel, List<ResponseListener> listeners )
    {
        this.channel = channel;
        this.listeners = listeners;
    }

    public void stop()
    {
        isRunning = false;
    }

    @Override
    public void run()
    {
        while( isRunning && channel.isOpen() )
        {
            try
            {
                AbstractResponse response = receiveResponse();
                logger.debug("Получен ответ.");
                if( response instanceof UpdateNotification )
                {
                    logger.debug("Тип: 'UpdateNotification'");
                    UpdateNotification update = (UpdateNotification) response;
                    SwingUtilities.invokeLater(() -> {
                        for (ResponseListener listener : listeners) {
                            listener.onCollectionUpdated(update.getData());
                        }
                    });
                }
                else
                {
                    logger.debug("Тип: 'responseCmdRes'");
                    ResponseCmdRes responseCmdRes = (ResponseCmdRes) response;
                    SwingUtilities.invokeLater( () -> {
                        for (ResponseListener listener : listeners)
                        {
                            listener.handleResponse(responseCmdRes);
                        }
                    });
                }

            }
            catch( IOException | ClassNotFoundException e )
            {
                if (isRunning)
                {
                    notifyError("Связь с сервером потеряна: " + e.getMessage());
                    stop();
                }
            }
        }
    }

    private AbstractResponse receiveResponse() throws IOException, ClassNotFoundException
    {
        ByteBuffer bufferLength = ByteBuffer.allocate(4);
        while( bufferLength.hasRemaining() )
        {
            int bytes = channel.read(bufferLength);
            checkBytes(bytes);
        }
        bufferLength.flip();
        int objectLength = bufferLength.getInt();

        ByteBuffer objectBuffer = ByteBuffer.allocate(objectLength);
        while (objectBuffer.hasRemaining())
        {
            int bytesRead = channel.read(objectBuffer);
            checkBytes(bytesRead);
        }
        objectBuffer.flip();

        byte[] data = new byte[objectBuffer.remaining()];
        objectBuffer.get(data);

        try( ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream) )
        {
            return (AbstractResponse) objectInputStream.readObject();
        }
    }

    private void checkBytes(int bytes) throws IOException
    {
        if (bytes == -1)
        {
            throw new IOException("Соединение разорвано сервером.");
        }
        if (bytes == 0)
        {
            try
            {
                Thread.sleep(50);
            }
            catch( InterruptedException e )
            {
                Thread.currentThread().interrupt();
                throw new IOException("Поток чтения был прерван.");
            }
        }
    }

    private void notifyError( String message )
    {
        SwingUtilities.invokeLater( () -> {
            for (ResponseListener listener : listeners)
            {
                listener.onError(message);
            }
        });
    }
}