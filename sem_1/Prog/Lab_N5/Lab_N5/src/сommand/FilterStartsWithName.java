package сommand;

import manager.CollectionManager;
import model.StudyGroup;
import myExceptions.CommandException;
import terminal.IO_Handler;

import java.util.Map;

/**
 * Команда для вывода элементов коллекции, отсортировав по началу названия элементов
 */
public class FilterStartsWithName implements CommandWithArgs
{
    private final CollectionManager collection;
    private String tempName;

    public FilterStartsWithName( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void getArgs( String Args )
    {
        try
        {
            tempName = Args;
        }
        catch (IllegalArgumentException e)
        {
            throw new CommandException("Неверно введен параметр: по данному ключу ничего не найдено");
        }
    }

    @Override
    public void execute( IO_Handler consol )
    {
        if( tempName == null || tempName.isEmpty() )
        {
            consol.printError("Введеная подстрока пустая!");
            return;
        }

        String flag = "";
        for( Map.Entry<Long, StudyGroup> element : collection.getStudyGroups().entrySet() )
        {
            if(element.getValue().getName().startsWith(tempName))
            {
                flag = element.getKey() + ": " + element.getValue().getName();
                consol.printInfo(flag);
                consol.printInfo("Подробная информация о " + element.getKey() + "-ом элеменете коллеции:\n" + element.getValue().getInformation());
            }
        }
        if( flag.isEmpty() )
        {
            consol.printInfo("Не найдено ни одного элемента с именем, начинающимся со строки '" + tempName + "'!");
        }
    }
    @Override
    public String getName()
    {
        return "filter_starts_with_name   { name }";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы, значение поля name которых начинается с заданной подстроки";
    }
}
