package manager;

import terminal.IO_Handler;
import сommand.Command;

import java.util.Map;

/**
 * Интерфейс, описывающий действия manager.Invoker
 */
public interface InvokerActions
{
    public void initIOput( IO_Handler ioHandler );
    public void addCommand( String name, Command newCommand );
    public void executeCommand( String input );
    public Map<String, Command> getCommands();
}
