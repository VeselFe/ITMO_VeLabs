package сommand;

import manager.CollectionManager;
import myExceptions.CreationException;
import terminal.IO_Handler;

import java.util.ArrayList;
import java.util.List;


/**
 * Команда для удаления элементов коллекции с меньшим ключом
 */
public class RomoveLowerKey implements CommandWithArgs
{
    private final CollectionManager collection;
    private Long Key;

    public RomoveLowerKey( CollectionManager newCollection )
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
            throw new CreationException("Некорректные данные для ключа");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        long count = 0;
        String deletedNames = "";
        List<Long> keys = new ArrayList<>(collection.getStudyGroups().keySet());

        try
        {
            for(Long id : keys)
            {
                if(id < Key)
                {
                    deletedNames = deletedNames + "\n" + (count+1) + ") " + collection.getStudyGroups().get(id).getName() +
                    "   'id' = " + id;
                    collection.getStudyGroups().remove(id);
                    count++;
                }
            }
            consol.printInfo("Было обнаружено и удалено " + count + " элемента(-ов): " + deletedNames);
        }
        catch (Exception e)
        {
            consol.printError("ошибка при удалении элементов с меньшим id " + e.getMessage());
        }
    }

    @Override
    public String getName()
    {
        return "remove_lower_key { key }";
    }
    @Override
    public String getDescription()
    {
        return "удалить из коллекции все элементы, ключ которых меньше, чем заданный";
    }
}
