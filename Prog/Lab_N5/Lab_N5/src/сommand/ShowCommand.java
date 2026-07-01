package сommand;

import manager.CollectionManager;
import model.StudyGroup;
import terminal.IO_Handler;
import java.util.Hashtable;
import java.util.Map;


/**
 * Команда вывода всех элементов коллекции в коносль
 */
public class ShowCommand implements Command
{
    private Hashtable<Long, StudyGroup> collection;
    public ShowCommand( CollectionManager newCollection )
    {
        collection = newCollection.getStudyGroups();
    }

    @Override
    public void execute( IO_Handler consol )
    {
        String flag = "";
        consol.printInfo("Элементы коллекции: ");
        consol.printInfo("***************************************");
        for (Map.Entry<Long, StudyGroup> element : collection.entrySet())
        {
            flag = element.getKey() + ": " + element.getValue().getName();
            consol.printInfo(flag);
            consol.printInfo("Подробная информация о " + element.getKey() + "-ом элеменете коллеции:\n" + element.getValue().getInformation());
        }
        if( flag == "" )
        {
            consol.printInfo("В колекции отсутствуют элементы!");
        }
        consol.printInfo("***************************************\n");
    }
    @Override
    public String getName()
    {
        return "show";
    }
    @Override
    public String getDescription()
    {
        return "вывести все элементы коллекции в строковом представлении";
    }

}
