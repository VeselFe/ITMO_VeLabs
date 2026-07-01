package ru.itmo.server.serverNetManager;

import org.slf4j.LoggerFactory;
import ru.itmo.lab.common.commonNet.AbstractResponse;
import ru.itmo.lab.common.commonNet.ResponseCmdRes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class ResponseCompiler
{
    public static byte[] compileResponse( AbstractResponse response ) throws IOException
    {
        byte[] responseBytes;
        if( response == null )
            throw new IOException("Был сгенерирован пустой ответ");

        try( ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream) )
        {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            responseBytes = byteOutputStream.toByteArray();
        }

        ByteBuffer fullBuffer = ByteBuffer.allocate(4 + responseBytes.length);
        fullBuffer.putInt(responseBytes.length);
        fullBuffer.put(responseBytes);
        LoggerFactory.getLogger(ResponseCompiler.class).debug("Длина пакета: " + responseBytes.length);

        return fullBuffer.array();
    }
}
