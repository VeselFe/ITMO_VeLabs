package сommand;

import manager.CollectionManager;
import manager.GroupsFileManager;
import manager.Invoker;
import myExceptions.FileManagerException;
import terminal.IO_Handler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Команда для обработки и выполнения команд из скрипта
 */
public class ExecuteScriptCommand implements CommandWithArgs
{
    private String fileName;
    private final CollectionManager collection;
    private static List<String> openedFiles = new ArrayList<>();
    private String scriptArg;

    public ExecuteScriptCommand( CollectionManager newCollection )
    {
        collection = newCollection;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        List<String> commands = new GroupsFileManager( fileName ).loadScript();
        openedFiles.add( fileName );
        consol.printInfo("Обнаружено " + commands.size() + "строк в скрипте");
        consol.printInfo(" --- Выполнение скрипта ---");

        Invoker scriptInvoker = new Invoker( collection );
        scriptInvoker.initIOput( consol );
        for(String command : commands)
        {
            if(command.trim().startsWith("execute_script"))
            {
                scriptArg = command.substring(15).trim();
                try
                {
                    Path newPath = Paths.get(scriptArg).toAbsolutePath().normalize();
                    for( String openedFile : openedFiles )
                    {
                        Path openedFilePath = Paths.get(openedFile).toAbsolutePath().normalize();
                        if( Files.isSameFile(openedFilePath, newPath) )
                        {
                            consol.printError("обнаружена попытка рекурсивного запуска скрипта '" + scriptArg + "'");
                            openedFiles.remove(fileName);
                            return;
                        }
                    }
                }
                catch( IOException e )
                {
                    throw new FileManagerException("Возникла ошибка при проверке пути скрипта '" + scriptArg + "'");
                }
            }

            scriptInvoker.executeCommand( command );
        }
        openedFiles.remove(fileName);
        consol.printInfo(" --- Скрипт '" + fileName + "' выполнен ---");
    }

    @Override
    public void getArgs( String Args )
    {
        fileName = String.valueOf( Args );
    }
    @Override
    public String getName()
    {
        return "execute_script {file_name}";
    }
    @Override
    public String getDescription()
    {
        return "считать и исполнить* скрипт из указанного файла \n" +
               "(*Команды должны быть записаны в формате, принимаемом программой, см.подробнее в 'help')";
    }
}
