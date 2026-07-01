package manager;

import myExceptions.CommandException;
import terminal.IO_Handler;
import сommand.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для регистрации, обработки выполнения, передачи аргументов командам
 *
 */
public class Invoker implements InvokerActions
{
    /** хранит все используемые пользовательские команды    */
    private final Map<String, Command> commands;
    /** ссылка на обрабатываемую коллекцию    */
    private final CollectionManager ManagersCollection;
    /** обарботчик консоли    */
    private IO_Handler ioHandler;

    public Invoker( CollectionManager newManagersCollection )
    {
        ManagersCollection = newManagersCollection;
        commands = new HashMap<>();
        registerCommands();
    }

    /**
     * Инициализация обработчика консоли
     *
     * @param newIO_Handler
     */
    @Override
    public void initIOput( IO_Handler newIO_Handler )
    {
        ioHandler = newIO_Handler;
    }

    /**
     * Инициализация всех пользовательских команд
     */
    private void registerCommands()
    {
        /// регистрируем все команды
        addCommand("help", new HelpCommand(this));
        addCommand("info", new InfoCommand(ManagersCollection));
        addCommand("show", new ShowCommand(ManagersCollection));
        addCommand("insert_element", new InsertElCommand(ManagersCollection));
        addCommand("update_id", new UpdateIdCommand(ManagersCollection));
        addCommand("remove_id", new RemoveCommand(ManagersCollection));
        addCommand("clear", new ClearCommand(ManagersCollection));
        addCommand("save", new SaveCommand(ManagersCollection, "SavedCollection.txt"));
        addCommand("execute_script", new ExecuteScriptCommand(ManagersCollection));
        addCommand("exit", new ExitCommand());
        addCommand("remove_greater", new RemoveGreater(ManagersCollection));
        addCommand("remove_lower ", new RemoveLower(ManagersCollection));
        addCommand("remove_lower", new RomoveLowerKey(ManagersCollection));
        addCommand("filter_by_semester_enum", new FilterBySemesterEnum(ManagersCollection));
        addCommand("filter_starts_with_name", new FilterStartsWithName(ManagersCollection));
        addCommand("print_ascending", new PrintAscending(ManagersCollection));
    }

    /**
     * Добавление команды в таблицу всех пользовательских команд
     *
     * @param name
     * @param newCommand
     */
    @Override
    public void addCommand( String name, Command newCommand )
    {
        if ( commands.containsKey(name) )
        {
            throw new CommandException("Команда '" + name + "' уже существует");
        }
        commands.put(name, newCommand);
    }

    /**
     * Обработка команды из потока ввода
     * Запуск выполнения обнаруженной команды
     *
     * @param input
     */
    @Override
    public void executeCommand( String input )
    {
        if (input.trim().isEmpty())
        {
            ioHandler.printInfo("Пустая команда");
            return;
        }

        String[] args = input.trim().split("\\s+");
        String name = args[0];

        Command cmd = commands.get(name);

        if ( cmd == null )
        {
            ioHandler.printInfo("Неизвестная команда: '" + name + "'");
            return;
        }
        else
        {
            if( !(cmd instanceof CommandWithArgs) && args.length > 2 )
            {
                ioHandler.printError("Больше 1 аргумента команды");
                return;
            }
        }

        try {
            if( cmd instanceof CommandWithArgs )
            {
                if( args.length == 2 )
                {
                    CommandWithArgs cmdArgs = (CommandWithArgs) cmd;
                    cmdArgs.getArgs(args[1]);
                    cmdArgs.execute(ioHandler);
                }
                else
                {
                    throw new IllegalArgumentException("Некорректный ввод аргументов команды");
                }
            }
            else
            {
                cmd.execute(ioHandler);
            }
        }
        catch ( IllegalArgumentException e )
        {
            throw new IllegalArgumentException("Неверные аргументы: " + e.getMessage());
        }
        catch ( Exception e )
        {
            throw new CommandException("ошибка исполнения команды '" + name + "': " + e.getMessage());
        }
    }

    /**
     * Возвращает таблицу всех команд
     *
     * @return Map
     */
    @Override
    public Map<String, Command> getCommands()
    {
        return commands;
    }
}
