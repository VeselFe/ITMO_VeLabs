package ru.itmo.server.serverNetManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.lab.common.commonNet.AbstractResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConnectionRegistry
{
    private static final Set<OutputStream> clients = Collections.synchronizedSet(new HashSet<>());
    private static final Logger logger = LoggerFactory.getLogger(ConnectionRegistry.class);

    public static void addClient( OutputStream out ) { clients.add(out); }
    public static void removeClient( OutputStream out ) { clients.remove(out); }

    public static void broadcast( AbstractResponse response )
    {
        byte[] bytes;
        try
        {
            bytes = ResponseCompiler.compileResponse(response);
        }
        catch( IOException e )
        {
            logger.error("Ошибка при отправке рассылки: возникла ошибка при компиляции ответа сервера.");
            return;
        }

        synchronized(clients)
        {
            for(OutputStream out : clients)
            {
                try
                {
                    synchronized( out )
                    {
                        // Синхронизация по конкретному потоку
                        logger.info("Рассылка отправлена!");
                        out.write(bytes);
                        out.flush();
                    }
                }
                catch (IOException e)
                {
                    logger.error("Ошибка при отправке рассылки.");
                    removeClient(out);
                }
            }
        }
    }
}