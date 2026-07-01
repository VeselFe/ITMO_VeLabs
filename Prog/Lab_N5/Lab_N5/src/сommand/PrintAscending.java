package сommand;

import manager.CollectionManager;
import model.StudyGroup;
import terminal.IO_Handler;

/**
 * Команда для отсортированного по названию вывода всех элементов коллекции
 */
public class PrintAscending implements Command
{
    private CollectionManager collectionManager;
    public PrintAscending( CollectionManager newCollectionManager )
    {
        collectionManager = newCollectionManager;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        String flag = "";
        consol.printInfo("Элементы коллекции в отсортированном порядке: ");
        consol.printInfo("***************************************");
        for (StudyGroup element : collectionManager.getSortedCollection())
        {
            consol.printInfo(flag = element.getInformation());
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
        return "print_ascending";
    }
    @Override
    public String getDescription()
    {
        return "вывести элементы коллекции в порядке возрастания (по id)";
    }

}
