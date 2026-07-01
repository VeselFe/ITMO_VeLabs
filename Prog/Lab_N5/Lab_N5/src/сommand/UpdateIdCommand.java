package сommand;

import manager.CollectionManager;
import model.Person;
import myExceptions.CommandException;
import myRecords.FieldDescriptor;
import myRecords.Lab5FieldDescriptor;
import terminal.IO_Handler;

/**
 * Команда для обновления указанного пользователем поля элеменат коллекции
 */
public class UpdateIdCommand implements CommandWithArgs
{
    private final CollectionManager collection;
    private Long Key;

    public UpdateIdCommand(CollectionManager newCollection )
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
    public void execute( IO_Handler console )
    {
        boolean exit = false;
        while(!exit)
        {
            try
            {
                if( !collection.getStudyGroups().containsKey(Key) )
                {
                    console.printError("По данному ключу ничего не найдено");
                    exit = true;
                    return;
                }
                console.printInfo("Какой параметр Вы хотите обновить?\n" +
                        "===========================================");
                for (FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                {
                    console.printInfo(element.name());
                }
                console.printInfo("===========================================");
                console.printRequest("Введите название параметра: ");
                String name = console.readline().trim();

                if( name.trim().isEmpty() )
                {
                    throw new CommandException("Поле ввода пусто!");
                }
                name = name.toLowerCase();
                for(FieldDescriptor element : Lab5FieldDescriptor.UPDATED_FIELDS)
                {
                    if( name.equals(element.name().toLowerCase()) )
                    {
                        if( name.equals("group admin") )
                        {
                            console.printInfo(element.request());
                            // Создание нового админа
                            Person newAdmin = console.readPerson();
                            exit = collection.updateElement(Key, "admin", "", newAdmin);
                        }

                        console.printRequest(element.request());
                        String input = console.readline().trim();

                        exit = collection.updateElement(Key, element.name(), input, null);
                    }
                }
            }
            catch (Exception e)
            {
                console.printError("Ошибка при обновлении группы: \n" + e.getMessage());
            }
        }
        console.printInfo("Элемент коллекции успешно обновлен!");
    }

    @Override
    public String getName()
    {
        return "update_id";
    }
    @Override
    public String getDescription()
    {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }
}
