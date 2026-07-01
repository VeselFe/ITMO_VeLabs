package сommand;

import manager.CollectionManager;
import terminal.IO_Handler;

/**
 * Команда для очистки коллекции
 */
public class ClearCommand implements Command
{
    private CollectionManager collection;
    public ClearCommand( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        collection.clearCollection();
        consol.printInfo("Коллекция успешно очищена!");
    }
    @Override
    public String getName()
    {
        return "clear";
    }
    @Override
    public String getDescription()
    {
        return "очистить коллекцию";
    }
}
