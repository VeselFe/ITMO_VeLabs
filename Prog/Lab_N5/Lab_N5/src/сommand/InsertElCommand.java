package сommand;

import manager.CollectionManager;
import model.StudyGroup;
import myExceptions.CreationException;
import terminal.IO_Handler;

/**
 * Команда для добавления нового элемента в коллекции
 */
public class InsertElCommand implements CommandWithArgs
{
    private final CollectionManager collection;
    private Long Key;

    public InsertElCommand( CollectionManager newCollection )
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
        try
        {
            if(collection.getStudyGroups().get(Key) != null)
            {
                throw new CreationException("Элемент с таким ключем уже существует");
            }
            StudyGroup newGroup = consol.readNewStudyGroup();
            collection.addElement(Key, newGroup);
        }
        catch (Exception e)
        {
            consol.printError("*Ошибка при создании группы*\n" + e.getMessage());
        }
    }

    @Override
    public String getName()
    {
        return "insert_element";
    }
    @Override
    public String getDescription()
    {
        return "добавить новый элемент с заданным ключом";
    }
}
