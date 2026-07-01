package myExceptions;

import java.io.IOException;

public class FileManagerException extends RuntimeException
{
    public FileManagerException( String errMessage )
    {
        super("Ошибка при работе с файлом: " + errMessage);
    }
}
