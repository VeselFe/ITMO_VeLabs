package myExceptions;

public class CommandException extends RuntimeException
{
    public CommandException( String errMessage )
    {
        super("Ошибка выполнения команды - " + errMessage);
    }
}
