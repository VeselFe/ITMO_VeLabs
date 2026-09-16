package ru.itmo.client.network;

import ru.itmo.lab.common.commonNet.ResponseCmdRes;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ResponseReader
{
    public static ResponseCmdRes read(ObjectInputStream inputStream ) throws IOException, ClassNotFoundException
    {
        Object serverObject = inputStream.readObject();
        if( serverObject == null )
        {
            throw new IOException("Получен пустой пакет!");
        }
        return (ResponseCmdRes) serverObject;
    }
}
