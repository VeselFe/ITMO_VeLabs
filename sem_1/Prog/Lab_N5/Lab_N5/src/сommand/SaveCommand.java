package сommand;

import manager.CollectionManager;
import manager.GroupsFileManager;
import myExceptions.FileManagerException;
import terminal.IO_Handler;


/**
 * Команда для сохранении коллекции в файл
 */
public class SaveCommand implements Command
{
    private final CollectionManager collection;
    private final String fileName;
    private final GroupsFileManager fileManager;

    public SaveCommand( CollectionManager newCollection, String newFileName )
    {
        collection = newCollection;
        fileName = newFileName;
        fileManager = new GroupsFileManager( fileName );
    }

    @Override
    public void execute( IO_Handler consol )
    {
        try
        {
            fileManager.save( collection.getStudyGroups() );
        }
        catch ( Exception e )
        {
            throw new FileManagerException("ошибка сохранения коллекции в файл '" + fileName + "'");
        }
        finally
        {
            consol.printInfo("Коллекция успешно сохранена!");
        }
    }
    @Override
    public String getName()
    {
        return "save";
    }
    @Override
    public String getDescription()
    {
        return "сохранить коллекцию в файл";
    }
}
