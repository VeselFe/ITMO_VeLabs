package сommand;

import terminal.IO_Handler;

/**
 * Команда для выхода из программы
 */
public class ExitCommand implements Command
{
    public ExitCommand() {}

    @Override
    public void execute( IO_Handler consol )
    {
        consol.stop();
    }
    @Override
    public String getName()
    {
        return "exit";
    }
    @Override
    public String getDescription()
    {
        return "завершить программу (без сохранения в файл)";
    }
}
