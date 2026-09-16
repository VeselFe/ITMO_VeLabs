package сommand;

import terminal.IO_Handler;

/**
 * Интерфейс для класса пользовательской команды
 */
public interface Command
{
    void execute( IO_Handler ioHandler );
    String getName();
    String getDescription();
}
