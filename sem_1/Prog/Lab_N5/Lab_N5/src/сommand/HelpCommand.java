package сommand;

import manager.InvokerActions;
import terminal.*;

/**
 * Команда для вывода в консоль списка всех пользовательских команд
 */
public class HelpCommand implements Command
{
    private final InvokerActions invoker;

    public HelpCommand( InvokerActions newInvokerInterface )
    {
        invoker = newInvokerInterface;
    }

    @Override
    public void execute( IO_Handler consol )
    {
        consol.printInfo("Список всех возможных команд:\n" +
                "--------------------------------------------------------------");
        for( Command command : invoker.getCommands().values() )
        {
            consol.printInfo(command.getName() + ": " + command.getDescription());
        }
    }

    @Override
    public String getName()
    {
        return "help";
    }
    @Override
    public String getDescription()
    {
        return "вывести справку по доступным командам";
    }
}
