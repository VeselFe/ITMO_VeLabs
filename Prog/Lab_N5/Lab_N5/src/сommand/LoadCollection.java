package сommand;

import manager.CollectionManager;
import manager.GroupsFileManager;
import model.StudyGroup;
import myExceptions.FileManagerException;
import terminal.IO_Handler;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Hashtable;

/**
 * Класс, реализующий чтение и обработку коллекции из xml-файла
 */
public class LoadCollection
{
    private final CollectionManager collection;
    private final String fileName;
    private final GroupsFileManager fileManager;
    private final IO_Handler console;

    public LoadCollection(CollectionManager newCollection, String newFileName, IO_Handler newConsole )
    {
        collection = newCollection;
        fileName = newFileName;
        fileManager = new GroupsFileManager( fileName );
        console = newConsole;
    }

    public void loadNewCollection()
    {
        try
        {
            Hashtable<Long, StudyGroup> loadedCollection = fileManager.load();

            collection.getStudyGroups().clear();
            for (Long key : loadedCollection.keySet())
            {
                collection.getStudyGroups().put(key, loadedCollection.get( key ));
            }
            console.printInfo("Загружено " + collection.getStudyGroups().size() + " групп(-ы).\n");
        }
        catch( Exception e )
        {
            console.printError(e.getMessage());
        }
    }
}
