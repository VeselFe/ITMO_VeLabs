package сommand;

import manager.CollectionManager;
import terminal.IO_Handler;

/**
 * Команда для вывода информации о коллекции
 */
public class InfoCommand implements Command
{
    private CollectionManager CollInfo;
    public InfoCommand( CollectionManager collection )
    {
        CollInfo = collection;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        consol.printInfo(CollInfo.getInfo());
    }
    @Override
    public String getName()
    {
        return "info";
    }
    @Override
    public String getDescription()
    {
        return "вывести информацию о коллекции";
    }
}
