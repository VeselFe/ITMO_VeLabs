package сommand;

import manager.CollectionManager;
import myExceptions.CommandException;
import terminal.IO_Handler;

/**
 * Команда для удаления элемента коллекции по ключу
 */
public class RemoveCommand implements CommandWithArgs
{
    private final CollectionManager collection;
    private Long Key;

    public RemoveCommand( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            Key = Long.valueOf( Args );
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Некорректные данные для ключа");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        collection.removeElement(Key);
    }
    @Override
    public String getName()
    {
        return "remove_key";
    }
    @Override
    public String getDescription()
    {
        return "удалить элемент коллекции по его ключу";
    }
}
