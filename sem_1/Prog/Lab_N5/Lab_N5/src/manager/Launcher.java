package manager;

import myExceptions.FileManagerException;
import terminal.IO_Handler;
import сommand.LoadCollection;


/**
 * Класс для загрузки коллекции из файла по названию Пользователя
 */
final public class Launcher
{
    private final CollectionManager collection;
    private final IO_Handler console;

    public Launcher( CollectionManager newCollection, IO_Handler ioHandler )
    {
        collection = newCollection;
        console = ioHandler;
    }

    public void launchCollection()
    {
        try
        {
//            Boolean setting = true;
            console.printRequest("Введите название файла для загрузки коллекции: ");
//            while( setting )
//            {
                String collectionFileName = console.readline();
                if( collectionFileName.isEmpty() )
                {
                    console.printError("Вы ничего не ввели!");
                }
                new LoadCollection(collection, collectionFileName, console).loadNewCollection();
//            }
        }
        catch( Exception e )
        {
            throw new FileManagerException("Ошибка загрузки файла!" + e);
        }
    }
}
