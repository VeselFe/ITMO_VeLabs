package сommand;

import manager.CollectionManager;
import manager.StudyGroupByStudentsComparator;
import model.StudyGroup;
import myExceptions.CommandException;
import terminal.IO_Handler;

import java.util.Map;

/**
 * Команда для удаления элементов коллекции с большим количеством студентов
 */
public class RemoveGreater implements CommandWithArgs
{
    private final CollectionManager collection;
    private Long Key;
    private StudyGroup choosenGroup;

    public RemoveGreater( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            Key = Long.valueOf( Args );
            choosenGroup = collection.getStudyGroups().get(Key);
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен параметр: по данному ключу ничего не найдено");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( choosenGroup == null )
        {
            consol.printError("По данному ключу не найдено элементов!");
            return;
        }

        StudyGroupByStudentsComparator comparator = new StudyGroupByStudentsComparator();
        long count = 0;
        for(Map.Entry<Long, StudyGroup> element : collection.getStudyGroups().entrySet())
        {
            if( comparator.compare(element.getValue(), choosenGroup) > 0 )
            {
                collection.getStudyGroups().remove(element.getKey());
                count++;
            }
        }
        if( count == 0 )
        {
            consol.printInfo("Совпадений не найдено!");
        }
        else
        {
            consol.printInfo("Было найдено и удалено " + count + " превышающих элементов.");
        }
    }
    @Override
    public String getName()
    {
        return "remove_greater  { Key }";
    }
    @Override
    public String getDescription()
    {
        return "удалить из коллекции все элементы, которых количество студентов больше, чем в заданном";
    }
}
