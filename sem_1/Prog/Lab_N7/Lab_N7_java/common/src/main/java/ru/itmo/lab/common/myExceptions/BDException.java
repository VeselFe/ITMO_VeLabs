package ru.itmo.lab.common.myExceptions;

public class BDException extends RuntimeException
{
    public BDException( String errMessage )
    {
        super("Ошибка при работе с БД: " + errMessage);
    }
}
